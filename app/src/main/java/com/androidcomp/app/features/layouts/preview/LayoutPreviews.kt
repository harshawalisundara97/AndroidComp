package com.androidcomp.app.features.layouts.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** layout-row-weight — Row with 3 boxes using different weight ratios (1f, 2f, 1f). */
@Composable
fun RowWeightPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(0xFFBBDEFB), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { Text("1", color = Color(0xFF0D47A1)) }
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(0xFFC8E6C9), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { Text("2", color = Color(0xFF1B5E20)) }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(0xFFFFE0B2), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) { Text("1", color = Color(0xFFE65100)) }
    }
}

/** layout-box-stack — Box with 3 layered children at different alignments, demonstrating z-order. */
@Composable
fun BoxStackPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFFEDE7F6), RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(56.dp)
                .background(Color(0xFF7E57C2), RoundedCornerShape(10.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .size(28.dp)
                .background(Color(0xFFFF7043), CircleShape)
        )
        Text(
            text = "Box stacking",
            modifier = Modifier
                .align(Alignment.Center),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
