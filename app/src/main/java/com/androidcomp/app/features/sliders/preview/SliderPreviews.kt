package com.androidcomp.app.features.sliders.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

/** slider-basic: a real Slider bound to local state, with a live percentage label. */
@Composable
fun BasicSliderPreview() {
    var value by remember { mutableStateOf(0.5f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Value: ${(value * 100).roundToInt()}%")
        Slider(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** slider-range: a real RangeSlider bound to local state, with a live range label. */
@Composable
fun RangeSliderPreview() {
    var range by remember { mutableStateOf(0.2f..0.8f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Range: ${(range.start * 100).roundToInt()}% - ${(range.endInclusive * 100).roundToInt()}%")
        RangeSlider(
            value = range,
            onValueChange = { range = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
