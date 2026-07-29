package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object SensorComponentCatalog {

    private val accelerometer = ComponentSpec(
        id = "sensor-accelerometer",
        category = ComponentCategory.SENSORS,
        title = "SensorManager (Accelerometer)",
        overview = "Reads raw motion-sensor data (e.g. the accelerometer) via SensorManager and a " +
            "SensorEventListener, delivering a continuous stream of x/y/z readings used for " +
            "step counting, tilt/orientation, and gesture-based interactions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun rememberAccelerometerReading(context: Context): State<FloatArray> {
                    val reading = remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }

                    DisposableEffect(Unit) {
                        val sensorManager = context.getSystemService(SensorManager::class.java)
                        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                        val listener = object : SensorEventListener {
                            override fun onSensorChanged(event: SensorEvent) {
                                reading.value = event.values.clone()
                            }
                            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
                        }
                        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
                        onDispose { sensorManager.unregisterListener(listener) }
                    }
                    return reading
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            class TiltViewModel(private val sensorManager: SensorManager) : ViewModel() {
                private val _tilt = MutableStateFlow(0f)
                val tilt: StateFlow<Float> = _tilt.asStateFlow()

                private val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        _tilt.value = event.values[0]
                    }
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
                }
                // register/unregister the listener from the screen's DisposableEffect,
                // keeping the SensorManager lifecycle tied to Compose rather than the ViewModel.
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("sensorType", "Int", "required", "The sensor to read, e.g. Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE."),
            ComponentProperty("samplingPeriodUs", "Int", "SENSOR_DELAY_NORMAL", "How often readings arrive; SENSOR_DELAY_UI/GAME/FASTEST trade battery for responsiveness."),
            ComponentProperty("registerListener", "(SensorEventListener, Sensor, Int) -> Boolean", "n/a", "Starts delivering onSensorChanged callbacks at the given sampling rate."),
            ComponentProperty("unregisterListener", "(SensorEventListener) -> Unit", "n/a", "Stops delivery; must be called to avoid a listener leak and unnecessary battery drain.")
        ),
        events = listOf(
            "onSensorChanged(event) — fired repeatedly at the requested sampling rate with new x/y/z values.",
            "onAccuracyChanged(sensor, accuracy) — fired when the sensor's reported accuracy level changes."
        ),
        bestPractices = listOf(
            "Always unregister the listener when the composable/screen leaves composition (via DisposableEffect's onDispose) to avoid draining battery in the background.",
            "Use the coarsest SENSOR_DELAY that still meets your UX need — SENSOR_DELAY_UI is usually sufficient and far cheaper than SENSOR_DELAY_FASTEST."
        ),
        commonMistakes = listOf(
            "Registering a sensor listener without ever unregistering it, leaking the listener and continuously draining battery even when the screen is gone.",
            "Assuming getDefaultSensor() always returns non-null — many devices lack certain sensors and it can return null, which must be handled."
        ),
        accessibilityNotes = listOf(
            "Motion-based interactions (e.g. shake-to-undo) should always have a non-motion alternative for users who cannot perform physical gestures.",
            "Sensor-driven UI updates should still respect reduced-motion accessibility settings rather than animating aggressively regardless of user preference."
        ),
        performanceNotes = listOf(
            "High-frequency sensor callbacks (SENSOR_DELAY_FASTEST) can trigger excessive recompositions if piped directly into Compose state — consider throttling/debouncing.",
            "Batch sensor-driven state updates rather than triggering a full recomposition on every single onSensorChanged callback."
        ),
        relatedComponentIds = listOf("sensor-proximity"),
        minApi = 21
    )

    private val proximity = ComponentSpec(
        id = "sensor-proximity",
        category = ComponentCategory.SENSORS,
        title = "Proximity Sensor",
        overview = "Reports how close an object (typically the user's ear/hand) is to the device's " +
            "screen, commonly used to turn off the display during phone calls or to detect a " +
            "covered-screen gesture.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun rememberIsNear(context: Context): State<Boolean> {
                    val isNear = remember { mutableStateOf(false) }

                    DisposableEffect(Unit) {
                        val sensorManager = context.getSystemService(SensorManager::class.java)
                        val proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
                        val listener = object : SensorEventListener {
                            override fun onSensorChanged(event: SensorEvent) {
                                isNear.value = event.values[0] < (proximity?.maximumRange ?: 5f)
                            }
                            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
                        }
                        sensorManager.registerListener(listener, proximity, SensorManager.SENSOR_DELAY_NORMAL)
                        onDispose { sensorManager.unregisterListener(listener) }
                    }
                    return isNear
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("sensorType", "Int", "Sensor.TYPE_PROXIMITY", "Selects the proximity sensor from SensorManager."),
            ComponentProperty("maximumRange", "Float", "device-specific", "The sensor's max reportable distance in cm; many devices only report binary near/far."),
            ComponentProperty("values[0]", "Float", "n/a", "Distance reading in the SensorEvent; compare against maximumRange to infer near/far.")
        ),
        events = listOf("onSensorChanged(event) — fired when the reported proximity distance changes."),
        bestPractices = listOf(
            "Treat proximity as binary (near/far) rather than a precise distance — most hardware only supports two discrete states.",
            "Unregister the listener promptly once the near/far state is no longer needed (e.g. call has ended)."
        ),
        commonMistakes = listOf(
            "Assuming a continuous, precise distance value — most devices only ever report the sensor's minimum or maximum range.",
            "Using the proximity sensor for anything user-safety-critical without a manual override, since behavior varies significantly across OEMs."
        ),
        accessibilityNotes = listOf(
            "Never rely on proximity-based screen blanking as the sole way to prevent accidental touches — provide a manual lock/unlock affordance too."
        ),
        performanceNotes = listOf(
            "Proximity sensors sample infrequently by nature; SENSOR_DELAY_NORMAL is sufficient and avoids unnecessary wakeups."
        ),
        relatedComponentIds = listOf("sensor-accelerometer"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(accelerometer, proximity)
}
