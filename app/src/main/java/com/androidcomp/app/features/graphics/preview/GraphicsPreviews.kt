package com.androidcomp.app.features.graphics.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** graphics-canvas: a real Canvas drawing a few DrawScope shapes. */
@Composable
fun CanvasPreview() {
    Canvas(modifier = Modifier.size(120.dp)) {
        drawCircle(
            color = Color(0xFF6C5CE7),
            radius = size.minDimension / 3,
            center = Offset(size.width / 3, size.height / 3)
        )
        drawRect(
            color = Color(0xFF00B4D8),
            topLeft = Offset(size.width / 2, size.height / 2),
            size = androidx.compose.ui.geometry.Size(size.width / 3, size.height / 3)
        )
        drawLine(
            color = Color(0xFFFF6B6B),
            start = Offset(0f, size.height),
            end = Offset(size.width, 0f),
            strokeWidth = 6f
        )
    }
}

/** graphics-drawbehind: an Icon with a custom drawBehind decoration behind it. */
@Composable
fun DrawBehindPreview() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .drawBehind {
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = size.minDimension / 2
                )
                drawCircle(
                    color = Color(0xFFFFA000),
                    radius = size.minDimension / 2,
                    style = Stroke(width = 4f)
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Star,
            contentDescription = null,
            tint = Color(0xFF7A4E00)
        )
    }
}
