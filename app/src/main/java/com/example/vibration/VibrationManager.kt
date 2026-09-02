package com.example.vibration

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.core.VibrationFeedbackConfig
import com.example.core.VibrationMode
import com.example.signal_processing.ProcessedSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class VibrationManager(
    context: Context
) {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    var config: VibrationFeedbackConfig = VibrationFeedbackConfig()
    private var vibeJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    private var lastSignal: ProcessedSignal? = null

    @Volatile
    private var isRunning = false

    fun start() {
        if (isRunning) return
        isRunning = true
        startVibeLoop()
    }

    fun stop() {
        isRunning = false
        vibeJob?.cancel()
        vibeJob = null
        try {
            vibrator?.cancel()
        } catch (ignored: Exception) {}
    }

    fun onSignalUpdate(signal: ProcessedSignal) {
        lastSignal = signal
    }

    private fun startVibeLoop() {
        vibeJob?.cancel()
        vibeJob = scope.launch {
            while (isActive && isRunning) {
                val cfg = config
                val signal = lastSignal

                if (!cfg.isEnabled || cfg.mode == VibrationMode.OFF || signal == null || !signal.isThresholdExceeded) {
                    delay(100)
                    continue
                }

                val strengthNorm = (signal.signalStrengthPct / 100f).coerceIn(0f, 1f)

                when (cfg.mode) {
                    VibrationMode.SHORT_PULSE -> {
                        vibrateOnce(40)
                        delay(600)
                    }
                    VibrationMode.PROPORTIONAL_PULSE -> {
                        // Pulse speed scales with signal strength: strong signal = rapid pulses (100ms interval)
                        val pulseLength = (30 + strengthNorm * 30).toLong()
                        val pauseLength = (500 - strengthNorm * 420).toLong().coerceAtLeast(80L)
                        vibrateOnce(pulseLength)
                        delay(pauseLength)
                    }
                    VibrationMode.CONTINUOUS_INTENSITY -> {
                        val amplitude = (50 + strengthNorm * 200).toInt().coerceIn(1, 255)
                        vibrateWithAmplitude(80, amplitude)
                        delay(90)
                    }
                    VibrationMode.OFF -> delay(200)
                }
            }
        }
    }

    private fun vibrateOnce(durationMs: Long) {
        if (vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (ignored: Exception) {}
    }

    private fun vibrateWithAmplitude(durationMs: Long, amplitude: Int) {
        if (vibrator == null || !vibrator.hasVibrator()) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (ignored: Exception) {}
    }
}
