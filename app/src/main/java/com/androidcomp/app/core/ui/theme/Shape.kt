package com.androidcomp.app.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Explicit radii per component type (design system spec) — used directly by
// components whose desired radius doesn't line up with a single M3 Shapes slot.
object AppRadii {
    val card = 24.dp
    val button = 18.dp
    val input = 16.dp
    val bottomNav = 26.dp
    val dialog = 28.dp
}

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(AppRadii.input),
    large = RoundedCornerShape(AppRadii.card),
    extraLarge = RoundedCornerShape(AppRadii.dialog)
)
