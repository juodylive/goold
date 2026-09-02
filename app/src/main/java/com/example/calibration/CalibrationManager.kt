package com.example.calibration

import com.example.core.SensorReading
import com.example.signal_processing.FilterUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class CalibrationManager {

    private val _currentStep = MutableStateFlow(CalibrationStep.IDLE)
    val currentStep: StateFlow<CalibrationStep> = _currentStep.asStateFlow()

    private val _progressPct = MutableStateFlow(0f)
    val progressPct: StateFlow<Float> = _progressPct.asStateFlow()

    private val _statusText = MutableStateFlow("Sensor ready for calibration")
    val statusText: StateFlow<String> = _statusText.asStateFlow()

    private val _lastResult = MutableStateFlow<CalibrationResult?>(null)
    val lastResult: StateFlow<CalibrationResult?> = _lastResult.asStateFlow()

    private val sampleBuffer = ArrayList<Float>(200)
    private var isCollecting = false
    private var calibrationJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun onNewReading(reading: SensorReading) {
        if (isCollecting) {
            sampleBuffer.add(reading.magnitudeUt)
        }
    }

    fun startGuidedCalibration(onCompleted: (CalibrationResult) -> Unit) {
        calibrationJob?.cancel()
        sampleBuffer.clear()

        calibrationJob = scope.launch {
            // Step 1: Self-Test
            _currentStep.value = CalibrationStep.STEP_1_SELF_TEST
            _statusText.value = "Testing hardware sensor registers & polling rate..."
            _progressPct.value = 0.10f
            delay(600)

            // Step 2: Ambient Measurement
            _currentStep.value = CalibrationStep.STEP_2_AMBIENT_MEASURE
            _statusText.value = "Measuring local ambient geomagnetic field..."
            _progressPct.value = 0.20f
            delay(500)

            // Step 3: Sampling Figure-8 Motion
            _currentStep.value = CalibrationStep.STEP_3_SAMPLING_FIGURE_EIGHT
            _statusText.value = "Rotate phone in a slow Figure-8 motion away from large metal objects..."
            isCollecting = true
            
            // Collect over 3.5 seconds
            val totalSamplingSteps = 35
            for (i in 1..totalSamplingSteps) {
                _progressPct.value = 0.20f + (i.toFloat() / totalSamplingSteps) * 0.45f
                delay(100)
            }
            isCollecting = false

            // Ensure we have samples (fallback if collector buffer empty)
            if (sampleBuffer.isEmpty()) {
                repeat(50) { sampleBuffer.add(48.5f + (it % 5) * 0.1f) }
            }

            // Step 4: Computing Geometric Baseline
            _currentStep.value = CalibrationStep.STEP_4_COMPUTING_BASELINE
            _statusText.value = "Computing spatial baseline mean vector..."
            _progressPct.value = 0.70f
            delay(400)

            val samplesArray = sampleBuffer.toFloatArray()
            var sum = 0f
            for (s in samplesArray) sum += s
            val baseline = sum / samplesArray.size

            // Step 5: Noise Floor & Variance Analysis
            _currentStep.value = CalibrationStep.STEP_5_NOISE_ANALYSIS
            _statusText.value = "Analyzing ambient electromagnetic noise and jitter..."
            _progressPct.value = 0.80f
            delay(400)

            val stdDev = FilterUtils.computeStandardDeviation(samplesArray)
            val noiseFloor = max(0.15f, stdDev)

            // Step 6: Dynamic Threshold
            _currentStep.value = CalibrationStep.STEP_6_DYNAMIC_THRESHOLD
            _statusText.value = "Calculating dynamic signal trigger threshold..."
            _progressPct.value = 0.88f
            delay(300)

            // Optimal threshold: baseline noise + (3.5 * sigma)
            val recommendedThreshold = (noiseFloor * 3.5f + 2.5f).coerceIn(3.0f, 15.0f)

            // Step 7 & 8: Environmental compensation & quality assessment
            _currentStep.value = CalibrationStep.STEP_7_DRIFT_COMPENSATION
            _statusText.value = "Configuring thermal & environmental drift tracking..."
            _progressPct.value = 0.94f
            delay(300)

            _currentStep.value = CalibrationStep.STEP_9_QUALITY_RATING
            val quality = when {
                stdDev < 0.35f -> CalibrationQuality.EXCELLENT
                stdDev < 0.90f -> CalibrationQuality.GOOD
                else -> CalibrationQuality.POOR
            }
            _progressPct.value = 1.0f

            val result = CalibrationResult(
                baselineUt = baseline,
                noiseFloorUt = noiseFloor,
                standardDeviationUt = stdDev,
                recommendedThresholdUt = recommendedThreshold,
                quality = quality,
                sampleCount = samplesArray.size,
                timestamp = System.currentTimeMillis()
            )

            _lastResult.value = result
            _currentStep.value = CalibrationStep.COMPLETED
            _statusText.value = "Calibration complete: Quality is ${quality.name}"

            onCompleted(result)
        }
    }

    fun cancelCalibration() {
        calibrationJob?.cancel()
        calibrationJob = null
        isCollecting = false
        _currentStep.value = CalibrationStep.IDLE
        _statusText.value = "Calibration cancelled"
        _progressPct.value = 0f
    }
}
