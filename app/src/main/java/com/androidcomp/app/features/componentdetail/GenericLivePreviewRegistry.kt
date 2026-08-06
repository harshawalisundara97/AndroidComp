package com.androidcomp.app.features.componentdetail

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.animations.preview.AnimateFloatAsStatePreview
import com.androidcomp.app.features.animations.preview.AnimatedVisibilityPreview
import com.androidcomp.app.features.dialogs.preview.AlertDialogPreview
import com.androidcomp.app.features.dialogs.preview.FullScreenDialogPreview
import com.androidcomp.app.features.gestures.preview.DragGesturePreview
import com.androidcomp.app.features.gestures.preview.TapGesturePreview
import com.androidcomp.app.features.graphics.preview.CanvasPreview
import com.androidcomp.app.features.graphics.preview.DrawBehindPreview
import com.androidcomp.app.features.images.preview.AsyncImagePreview
import com.androidcomp.app.features.images.preview.PainterResourceImagePreview
import com.androidcomp.app.features.layouts.preview.BoxStackPreview
import com.androidcomp.app.features.layouts.preview.RowWeightPreview
import com.androidcomp.app.features.lists.preview.LazyColumnPreview
import com.androidcomp.app.features.lists.preview.LazyRowPreview
import com.androidcomp.app.features.materialcomponents.preview.BadgePreview
import com.androidcomp.app.features.materialcomponents.preview.CardPreview
import com.androidcomp.app.features.materialcomponents.preview.FilterChipPreview
import com.androidcomp.app.features.menus.preview.DropdownMenuPreview
import com.androidcomp.app.features.menus.preview.ExposedDropdownMenuPreview
import com.androidcomp.app.features.navigation.preview.BottomNavBarPreview
import com.androidcomp.app.features.navigation.preview.NavHostPreview
import com.androidcomp.app.features.sliders.preview.BasicSliderPreview
import com.androidcomp.app.features.sliders.preview.RangeSliderPreview
import com.androidcomp.app.features.textinputs.preview.FilledTextFieldPreview
import com.androidcomp.app.features.textinputs.preview.OutlinedTextFieldPreview

/**
 * Live previews for categories that don't have a dedicated playground (state +
 * interactive controls) of their own — each entry here is a single self-contained
 * composable that manages its own local UI state, so it's fully interactive on its
 * own without needing a shared ViewModel/registry pair like Buttons/Text/etc do.
 */
object GenericLivePreviewRegistry {
    val previews: Map<String, @Composable () -> Unit> = mapOf(
        "textinput-outlined" to { OutlinedTextFieldPreview() },
        "textinput-filled" to { FilledTextFieldPreview() },
        "image-painter-resource" to { PainterResourceImagePreview() },
        "image-async" to { AsyncImagePreview() },
        "layout-row-weight" to { RowWeightPreview() },
        "layout-box-stack" to { BoxStackPreview() },
        "list-lazy-column" to { LazyColumnPreview() },
        "list-lazy-row" to { LazyRowPreview() },
        "nav-bottom-bar" to { BottomNavBarPreview() },
        "nav-host" to { NavHostPreview() },
        "dialog-alert" to { AlertDialogPreview() },
        "dialog-fullscreen" to { FullScreenDialogPreview() },
        "menu-dropdown" to { DropdownMenuPreview() },
        "menu-exposed-dropdown" to { ExposedDropdownMenuPreview() },
        "material-card" to { CardPreview() },
        "material-chip" to { FilterChipPreview() },
        "material-badge" to { BadgePreview() },
        "slider-basic" to { BasicSliderPreview() },
        "slider-range" to { RangeSliderPreview() },
        "gesture-tap" to { TapGesturePreview() },
        "gesture-drag" to { DragGesturePreview() },
        "animation-visibility" to { AnimatedVisibilityPreview() },
        "animation-float-state" to { AnimateFloatAsStatePreview() },
        "graphics-canvas" to { CanvasPreview() },
        "graphics-drawbehind" to { DrawBehindPreview() }
    )
}
