package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.core.AudioFeedbackConfig
import com.example.core.ToneType
import com.example.signal_processing.ProcessedSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class AudioFeedbackEngine {

    private val sampleRate = 22050
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var synthJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var config: AudioFeedbackConfig = AudioFeedbackConfig()

    @Volatile
    private var currentSignalStrengthPct: Float = 0f

    @Volatile
    private var currentDeltaUt: Float = 0f

    @Volatile
    private var isThresholdExceeded: Boolean = false

    fun start() {
        if (isPlaying) return
        try {
            val minBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val bufferSize = minBufferSize * 2

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()
            isPlaying = true
            startSynthesizerLoop(bufferSize)
        } catch (ignored: Exception) {}
    }

    fun stop() {
        isPlaying = false
        synthJob?.cancel()
        synthJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (ignored: Exception) {}
        audioTrack = null
    }

    fun updateSignal(signal: ProcessedSignal) {
        currentSignalStrengthPct = signal.signalStrengthPct
        currentDeltaUt = signal.deltaUt
        isThresholdExceeded = signal.isThresholdExceeded
    }

    private fun startSynthesizerLoop(bufferSize: Int) {
        synthJob?.cancel()
        synthJob = scope.launch {
            val shortBuffer = ShortArray(512)
            var phase = 0.0
            var pulseTick = 0

            while (isActive && isPlaying) {
                val cfg = config
                if (!cfg.isEnabled || currentSignalStrengthPct < cfg.minSignalThresholdPct) {
                    // Mute / Zero buffer
                    shortBuffer.fill(0)
                    audioTrack?.write(shortBuffer, 0, shortBuffer.size)
                    continue
                }

                val strengthNorm = (currentSignalStrengthPct / 100f).coerceIn(0f, 1f)
                val targetFreq = (cfg.minPitchHz + (cfg.maxPitchHz - cfg.minPitchHz) * strengthNorm).toDouble()
                val vol = (cfg.volume * (0.3f + strengthNorm * 0.7f)).coerceIn(0f, 1f)
                val maxAmp = (Short.MAX_VALUE * vol).toInt()

                when (cfg.toneType) {
                    ToneType.VCO_CONTINUOUS -> {
                        val phaseIncrement = (2.0 * Math.PI * targetFreq) / sampleRate
                        for (i in shortBuffer.indices) {
                            val sample = (sin(phase) * maxAmp).toInt().toShort()
                            shortBuffer[i] = sample
                            phase += phaseIncrement
                            if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                        }
                    }
                    ToneType.PULSED_CLICKER -> {
                        // Click rate increases with signal strength (from 2 clicks/s up to 30 clicks/s)
                        val clicksPerSecond = (2f + strengthNorm * 28f).toInt()
                        val samplesPerClick = sampleRate / clicksPerSecond.coerceAtLeast(1)
                        val clickDurationSamples = 120 // ~5ms click

                        for (i in shortBuffer.indices) {
                            pulseTick++
                            val inClick = (pulseTick % samplesPerClick) < clickDurationSamples
                            if (inClick) {
                                val clickPhase = (pulseTick % samplesPerClick).toDouble() / clickDurationSamples
                                val clickSample = (sin(clickPhase * Math.PI * 4.0) * maxAmp).toInt().toShort()
                                shortBuffer[i] = clickSample
                            } else {
                                shortBuffer[i] = 0
                            }
                        }
                    }
                    ToneType.MULTI_TONE -> {
                        // Stepped discrete tones
                        val steppedFreq = when {
                            strengthNorm < 0.25f -> cfg.minPitchHz
                            strengthNorm < 0.50f -> cfg.minPitchHz * 1.5f
                            strengthNorm < 0.75f -> cfg.minPitchHz * 2.2f
                            else -> cfg.maxPitchHz
                        }.toDouble()

                        val phaseIncrement = (2.0 * Math.PI * steppedFreq) / sampleRate
                        for (i in shortBuffer.indices) {
                            val sample = (sin(phase) * maxAmp).toInt().toShort()
                            shortBuffer[i] = sample
                            phase += phaseIncrement
                            if (phase > 2.0 * Math.PI) phase -= 2.0 * Math.PI
                        }
                    }
                }

                audioTrack?.write(shortBuffer, 0, shortBuffer.size)
            }
        }
    }
}
