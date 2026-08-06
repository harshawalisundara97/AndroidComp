package com.androidcomp.app.features.animations.preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** animation-visibility: a Button toggling an AnimatedVisibility card. */
@Composable
fun AnimatedVisibilityPreview() {
    var visible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { visible = !visible }) {
            Text("Toggle")
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                    "Animated content",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

/** animation-float-state: a Button driving an animateFloatAsState-controlled Box size. */
@Composable
fun AnimateFloatAsStatePreview() {
    var toggled by remember { mutableStateOf(false) }
    val size by animateFloatAsState(if (toggled) 120f else 60f, label = "animFloatSize")

    Column {
        Button(onClick = { toggled = !toggled }) {
            Text("Animate")
        }
        Box(
            modifier = Modifier.padding(top = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .background(Color(0xFF00B4D8), RoundedCornerShape(16.dp))
            )
        }
    }
}
