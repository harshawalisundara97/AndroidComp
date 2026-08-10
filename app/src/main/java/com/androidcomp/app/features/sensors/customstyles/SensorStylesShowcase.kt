package com.androidcomp.app.features.sensors.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun SensorStylesShowcase(state: SensorStylesState, viewModel: SensorStylesViewModel) {
    Column(Modifier.fillMaxWidth()) {
        SensorStyleRow(
            "1. Accelerometer Gauge",
            "A rotating needle gauge, updates automatically on a timer.",
            { AccelerometerGauge(state.accelerometerAngle) },
            accelerometerCode
        )
        SensorStyleRow(
            "2. Compass Heading Indicator",
            "A rotating compass dial that animates to a simulated heading.",
            { CompassHeadingIndicator(state.compassHeading, viewModel::simulateCompassReading) },
            compassCode
        )
        SensorStyleRow(
            "3. Step Counter Card",
            "Progress ring toward a daily step goal.",
            { StepCounterCard(state.stepCount, state.stepGoal, viewModel::simulateStep) },
            stepCounterCode
        )
        SensorStyleRow(
            "4. Light Sensor Indicator",
            "A brightness bar that animates to a simulated ambient light reading.",
            { LightSensorIndicator(state.brightnessLevel, viewModel::simulateLightReading) },
            lightSensorCode
        )
        SensorStyleRow(
            "5. Proximity Status Card",
            "A toggling near/far status card with a crossfade icon.",
            { ProximityStatusCard(state.isNear, viewModel::toggleProximity) },
            proximityCode
        )
    }
}

@Composable
private fun SensorStyleRow(
    title: String,
    description: String,
    content: @Composable () -> Unit,
    code: String
) {
    Column(Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )
        Column(Modifier.padding(bottom = 10.dp)) { content() }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val accelerometerCode = """
    val needleAngle by animateFloatAsState(angle, spring(Spring.DampingRatioMediumBouncy))
    Canvas(Modifier.size(120.dp)) {
        // draw dial background + needle rotated by needleAngle
        rotate(needleAngle) { drawLine(/* needle */) }
    }
""".trimIndent()

private val compassCode = """
    val rotation by animateFloatAsState(heading, spring())
    Box {
        Icon(
            imageVector = Icons.Outlined.Navigation,
            contentDescription = null,
            modifier = Modifier.rotate(-rotation)
        )
    }
    Button(onClick = onSimulate) { Text("Simulate Reading") }
""".trimIndent()

private val stepCounterCode = """
    val progress by animateFloatAsState(stepCount / stepGoal.toFloat())
    CircularProgressIndicator(progress = { progress })
    Text("${'$'}stepCount / ${'$'}stepGoal steps")
    Button(onClick = onSimulateStep) { Text("Simulate Step") }
""".trimIndent()

private val lightSensorCode = """
    val width by animateDpAsState(maxWidth * brightness)
    Box(Modifier.fillMaxWidth().height(12.dp).background(Color(0xFFF2F2F2))) {
        Box(Modifier.width(width).fillMaxHeight().background(MaterialTheme.colorScheme.tertiary))
    }
    Button(onClick = onSimulate) { Text("Simulate Reading") }
""".trimIndent()

private val proximityCode = """
    Crossfade(targetState = isNear) { near ->
        Icon(
            imageVector = if (near) Icons.Outlined.Sensors else Icons.Outlined.SensorsOff,
            contentDescription = null
        )
    }
    Text(if (isNear) "Object Near" else "Object Far")
    Button(onClick = onToggle) { Text("Toggle") }
""".trimIndent()
