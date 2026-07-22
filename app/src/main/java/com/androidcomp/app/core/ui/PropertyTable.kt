package com.androidcomp.app.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.domain.model.ComponentProperty

@Composable
fun PropertyTable(properties: List<ComponentProperty>) {
    Column(Modifier.fillMaxWidth()) {
        properties.forEach { property ->
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.weight(1f)) {
                    Text(property.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${property.type} (default: ${property.defaultValue})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(property.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
