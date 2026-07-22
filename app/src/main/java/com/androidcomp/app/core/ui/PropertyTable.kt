package com.androidcomp.app.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.theme.AppRadii
import com.androidcomp.app.core.ui.theme.LocalAppColors
import com.androidcomp.app.domain.model.ComponentProperty

@Composable
fun PropertyTable(properties: List<ComponentProperty>) {
    val border = LocalAppColors.current.border

    OutlinedCard(
        shape = RoundedCornerShape(AppRadii.card),
        border = BorderStroke(1.dp, border),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            properties.forEachIndexed { index, property ->
                if (index > 0) {
                    androidx.compose.material3.HorizontalDivider(color = border)
                }
                Column(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    Text(
                        property.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${property.type} (default: ${property.defaultValue})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        property.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
