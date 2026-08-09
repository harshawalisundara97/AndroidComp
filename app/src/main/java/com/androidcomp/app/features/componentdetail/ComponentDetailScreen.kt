package com.androidcomp.app.features.componentdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidcomp.app.core.ui.AppTopBar
import com.androidcomp.app.core.ui.BulletList
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.core.ui.PropertyTable
import com.androidcomp.app.core.ui.SectionHeader
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.features.buttons.playground.PlaygroundControls
import com.androidcomp.app.features.buttons.playground.PlaygroundViewModel
import com.androidcomp.app.features.buttons.preview.ButtonPreviewRegistry
import com.androidcomp.app.features.text.playground.TextPlaygroundControls
import com.androidcomp.app.features.text.playground.TextPlaygroundViewModel
import com.androidcomp.app.features.text.preview.TextPreviewRegistry
import com.androidcomp.app.features.selectioncontrols.playground.SelectionPlaygroundControls
import com.androidcomp.app.features.selectioncontrols.playground.SelectionPlaygroundViewModel
import com.androidcomp.app.features.selectioncontrols.preview.SelectionPreviewRegistry
import com.androidcomp.app.features.progress.playground.ProgressPlaygroundControls
import com.androidcomp.app.features.progress.playground.ProgressPlaygroundViewModel
import com.androidcomp.app.features.progress.preview.ProgressPreviewRegistry
import com.androidcomp.app.features.selectioncontrols.toggles.ToggleStylesShowcase
import com.androidcomp.app.features.selectioncontrols.toggles.ToggleStylesViewModel
import com.androidcomp.app.features.buttons.customstyles.ButtonLoadState
import com.androidcomp.app.features.buttons.customstyles.CustomButtonsShowcase
import com.androidcomp.app.features.buttons.customstyles.CustomButtonsViewModel
import com.androidcomp.app.features.materialcomponents.cardstyles.CardStylesShowcase
import com.androidcomp.app.features.materialcomponents.cardstyles.CardStylesViewModel
import com.androidcomp.app.features.text.customstyles.CustomTextsShowcase
import com.androidcomp.app.features.text.customstyles.CustomTextsViewModel
import com.androidcomp.app.features.textinputs.customstyles.CustomTextInputsShowcase
import com.androidcomp.app.features.textinputs.customstyles.CustomTextInputsViewModel
import com.androidcomp.app.features.images.customstyles.CustomImagesShowcase
import com.androidcomp.app.features.images.customstyles.CustomImagesViewModel
import com.androidcomp.app.features.layouts.customstyles.LayoutStylesShowcase
import com.androidcomp.app.features.layouts.customstyles.LayoutStylesViewModel
import com.androidcomp.app.features.lists.customstyles.ListStylesShowcase
import com.androidcomp.app.features.lists.customstyles.ListStylesViewModel
import com.androidcomp.app.features.maps.customstyles.MapStylesShowcase
import com.androidcomp.app.features.maps.customstyles.MapStylesViewModel
import com.androidcomp.app.features.media.customstyles.MediaStylesShowcase
import com.androidcomp.app.features.media.customstyles.MediaStylesViewModel
import com.androidcomp.app.features.menus.customstyles.MenuStylesShowcase
import com.androidcomp.app.features.menus.customstyles.MenuStylesViewModel
import com.androidcomp.app.features.navigation.customstyles.NavStylesShowcase
import com.androidcomp.app.features.navigation.customstyles.NavStylesViewModel
import com.androidcomp.app.features.networking.customstyles.NetworkingStylesShowcase
import com.androidcomp.app.features.networking.customstyles.NetworkingStylesViewModel
import com.androidcomp.app.features.permissions.customstyles.PermissionStylesShowcase
import com.androidcomp.app.features.permissions.customstyles.PermissionStylesViewModel
import com.androidcomp.app.features.progress.customstyles.ProgressStylesShowcase
import com.androidcomp.app.features.progress.customstyles.ProgressStylesViewModel
import com.androidcomp.app.features.storage.customstyles.StorageStylesShowcase
import com.androidcomp.app.features.storage.customstyles.StorageStylesViewModel
import com.androidcomp.app.features.animations.customstyles.AnimationStylesShowcase
import com.androidcomp.app.features.animations.customstyles.AnimationStylesViewModel
import com.androidcomp.app.features.gestures.customstyles.GestureStylesShowcase
import com.androidcomp.app.features.gestures.customstyles.GestureStylesViewModel
import com.androidcomp.app.features.graphics.customstyles.GraphicsStylesShowcase
import com.androidcomp.app.features.graphics.customstyles.GraphicsStylesViewModel
import com.androidcomp.app.features.dialogs.customstyles.DialogStylesShowcase
import com.androidcomp.app.features.dialogs.customstyles.DialogStylesViewModel
import com.androidcomp.app.features.selectioncontrols.customstyles.SelectionStylesShowcase
import com.androidcomp.app.features.selectioncontrols.customstyles.SelectionStylesViewModel
import com.androidcomp.app.features.sensors.customstyles.SensorStylesShowcase
import com.androidcomp.app.features.sensors.customstyles.SensorStylesViewModel
import com.androidcomp.app.features.camera.customstyles.CameraStylesShowcase
import com.androidcomp.app.features.camera.customstyles.CameraStylesViewModel
import com.androidcomp.app.features.sliders.customstyles.SliderStylesShowcase
import com.androidcomp.app.features.sliders.customstyles.SliderStylesViewModel
import com.androidcomp.app.features.notifications.customstyles.NotificationStylesShowcase
import com.androidcomp.app.features.notifications.customstyles.NotificationStylesViewModel
import com.androidcomp.app.features.materialcomponents.customstyles.MaterialStylesShowcase
import com.androidcomp.app.features.materialcomponents.customstyles.MaterialStylesViewModel

@Composable
fun ComponentDetailScreen(
    onRelatedComponentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    detailViewModel: ComponentDetailViewModel = hiltViewModel(),
    buttonPlaygroundViewModel: PlaygroundViewModel = hiltViewModel(),
    textPlaygroundViewModel: TextPlaygroundViewModel = hiltViewModel(),
    selectionPlaygroundViewModel: SelectionPlaygroundViewModel = hiltViewModel(),
    progressPlaygroundViewModel: ProgressPlaygroundViewModel = hiltViewModel(),
    toggleStylesViewModel: ToggleStylesViewModel = hiltViewModel(),
    customButtonsViewModel: CustomButtonsViewModel = hiltViewModel(),
    cardStylesViewModel: CardStylesViewModel = hiltViewModel(),
    customTextsViewModel: CustomTextsViewModel = hiltViewModel(),
    customTextInputsViewModel: CustomTextInputsViewModel = hiltViewModel(),
    customImagesViewModel: CustomImagesViewModel = hiltViewModel(),
    layoutStylesViewModel: LayoutStylesViewModel = hiltViewModel(),
    listStylesViewModel: ListStylesViewModel = hiltViewModel(),
    mapStylesViewModel: MapStylesViewModel = hiltViewModel(),
    mediaStylesViewModel: MediaStylesViewModel = hiltViewModel(),
    menuStylesViewModel: MenuStylesViewModel = hiltViewModel(),
    navStylesViewModel: NavStylesViewModel = hiltViewModel(),
    networkingStylesViewModel: NetworkingStylesViewModel = hiltViewModel(),
    permissionStylesViewModel: PermissionStylesViewModel = hiltViewModel(),
    progressStylesViewModel: ProgressStylesViewModel = hiltViewModel(),
    storageStylesViewModel: StorageStylesViewModel = hiltViewModel(),
    animationStylesViewModel: AnimationStylesViewModel = hiltViewModel(),
    gestureStylesViewModel: GestureStylesViewModel = hiltViewModel(),
    graphicsStylesViewModel: GraphicsStylesViewModel = hiltViewModel(),
    dialogStylesViewModel: DialogStylesViewModel = hiltViewModel(),
    selectionStylesViewModel: SelectionStylesViewModel = hiltViewModel(),
    sensorStylesViewModel: SensorStylesViewModel = hiltViewModel(),
    cameraStylesViewModel: CameraStylesViewModel = hiltViewModel(),
    sliderStylesViewModel: SliderStylesViewModel = hiltViewModel(),
    notificationStylesViewModel: NotificationStylesViewModel = hiltViewModel(),
    materialStylesViewModel: MaterialStylesViewModel = hiltViewModel()
) {
    val spec by detailViewModel.spec.collectAsState()

    Scaffold(topBar = { AppTopBar(spec?.title ?: "Component", onBackClick = onBackClick) }) { padding ->
        val currentSpec = spec
        if (currentSpec == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Component not found")
            }
            return@Scaffold
        }

        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            item {
                ComponentDetailContent(
                    currentSpec,
                    buttonPlaygroundViewModel,
                    textPlaygroundViewModel,
                    selectionPlaygroundViewModel,
                    progressPlaygroundViewModel,
                    toggleStylesViewModel,
                    customButtonsViewModel,
                    cardStylesViewModel,
                    customTextsViewModel,
                    customTextInputsViewModel,
                    customImagesViewModel,
                    layoutStylesViewModel,
                    listStylesViewModel,
                    mapStylesViewModel,
                    mediaStylesViewModel,
                    menuStylesViewModel,
                    navStylesViewModel,
                    networkingStylesViewModel,
                    permissionStylesViewModel,
                    progressStylesViewModel,
                    storageStylesViewModel,
                    animationStylesViewModel,
                    gestureStylesViewModel,
                    graphicsStylesViewModel,
                    dialogStylesViewModel,
                    selectionStylesViewModel,
                    sensorStylesViewModel,
                    cameraStylesViewModel,
                    sliderStylesViewModel,
                    notificationStylesViewModel,
                    materialStylesViewModel,
                    onRelatedComponentClick
                )
            }
        }
    }
}

@Composable
private fun ComponentDetailContent(
    spec: ComponentSpec,
    buttonPlaygroundViewModel: PlaygroundViewModel,
    textPlaygroundViewModel: TextPlaygroundViewModel,
    selectionPlaygroundViewModel: SelectionPlaygroundViewModel,
    progressPlaygroundViewModel: ProgressPlaygroundViewModel,
    toggleStylesViewModel: ToggleStylesViewModel,
    customButtonsViewModel: CustomButtonsViewModel,
    cardStylesViewModel: CardStylesViewModel,
    customTextsViewModel: CustomTextsViewModel,
    customTextInputsViewModel: CustomTextInputsViewModel,
    customImagesViewModel: CustomImagesViewModel,
    layoutStylesViewModel: LayoutStylesViewModel,
    listStylesViewModel: ListStylesViewModel,
    mapStylesViewModel: MapStylesViewModel,
    mediaStylesViewModel: MediaStylesViewModel,
    menuStylesViewModel: MenuStylesViewModel,
    navStylesViewModel: NavStylesViewModel,
    networkingStylesViewModel: NetworkingStylesViewModel,
    permissionStylesViewModel: PermissionStylesViewModel,
    progressStylesViewModel: ProgressStylesViewModel,
    storageStylesViewModel: StorageStylesViewModel,
    animationStylesViewModel: AnimationStylesViewModel,
    gestureStylesViewModel: GestureStylesViewModel,
    graphicsStylesViewModel: GraphicsStylesViewModel,
    dialogStylesViewModel: DialogStylesViewModel,
    selectionStylesViewModel: SelectionStylesViewModel,
    sensorStylesViewModel: SensorStylesViewModel,
    cameraStylesViewModel: CameraStylesViewModel,
    sliderStylesViewModel: SliderStylesViewModel,
    notificationStylesViewModel: NotificationStylesViewModel,
    materialStylesViewModel: MaterialStylesViewModel,
    onRelatedComponentClick: (String) -> Unit
) {
    Column {

        // 1. Overview
        SectionHeader("Overview")
        Text(spec.overview)

        // 2 & 3. Live Preview + Interactive Playground — only for components with a
        // registered live preview. Each category with a playground gets its own state
        // holder and controls (a button's label/enabled and text's content/bold/italic
        // aren't the same shape); categories without one skip these two sections
        // rather than showing an empty/irrelevant preview and playground controls.
        when {
            ButtonPreviewRegistry.previews.containsKey(spec.id) -> {
                val playgroundState by buttonPlaygroundViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                ButtonPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

                SectionHeader("Interactive Playground")
                PlaygroundControls(
                    state = playgroundState,
                    onLabelChange = buttonPlaygroundViewModel::setLabel,
                    onEnabledChange = buttonPlaygroundViewModel::setEnabled
                )
            }
            TextPreviewRegistry.previews.containsKey(spec.id) -> {
                val playgroundState by textPlaygroundViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                TextPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

                SectionHeader("Interactive Playground")
                TextPlaygroundControls(
                    state = playgroundState,
                    onTextChange = textPlaygroundViewModel::setText,
                    onBoldChange = textPlaygroundViewModel::setBold,
                    onItalicChange = textPlaygroundViewModel::setItalic
                )
            }
            SelectionPreviewRegistry.previews.containsKey(spec.id) -> {
                val playgroundState by selectionPlaygroundViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                SelectionPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

                SectionHeader("Interactive Playground")
                SelectionPlaygroundControls(
                    state = playgroundState,
                    onCheckedChange = selectionPlaygroundViewModel::setChecked,
                    onEnabledChange = selectionPlaygroundViewModel::setEnabled
                )
            }
            ProgressPreviewRegistry.previews.containsKey(spec.id) -> {
                val playgroundState by progressPlaygroundViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                ProgressPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

                SectionHeader("Interactive Playground")
                ProgressPlaygroundControls(
                    state = playgroundState,
                    onProgressChange = progressPlaygroundViewModel::setProgress
                )
            }
            spec.id == "selection-toggle-styles" -> {
                val toggleStylesState by toggleStylesViewModel.state.collectAsState()

                // This showcase renders 5 self-contained, independently tappable toggles —
                // the interaction IS the live preview, so there's no separate generic
                // "Interactive Playground" controls section here.
                SectionHeader("Live Preview")
                ToggleStylesShowcase(
                    state = toggleStylesState,
                    onFluidSpringChange = toggleStylesViewModel::setFluidSpring,
                    onDayNightChange = toggleStylesViewModel::setDayNight,
                    onCyberpunkNeonChange = toggleStylesViewModel::setCyberpunkNeon,
                    onNeumorphicChange = toggleStylesViewModel::setNeumorphic,
                    onElasticPillChange = toggleStylesViewModel::setElasticPill
                )
            }
            spec.id == "button-custom-styles" -> {
                val customButtonsState by customButtonsViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CustomButtonsShowcase(
                    loadState = customButtonsState.loadState,
                    onLoadingButtonClick = customButtonsViewModel::startLoadingDemo
                )
            }
            spec.id == "material-card-styles" -> {
                val cardStylesState by cardStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CardStylesShowcase(
                    state = cardStylesState,
                    onStatCardIncrement = cardStylesViewModel::incrementStat,
                    onImageCardToggleSaved = cardStylesViewModel::toggleImageSaved
                )
            }
            spec.id == "text-custom-styles" -> {
                val state by customTextsViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CustomTextsShowcase(
                    state = state,
                    onCycleGradient = customTextsViewModel::cycleGradient,
                    onToggleExpanded = customTextsViewModel::toggleExpanded,
                    onTriggerCounter = customTextsViewModel::triggerCounter,
                    onToggleHighlight = customTextsViewModel::toggleHighlight
                )
            }
            spec.id == "textinput-custom-styles" -> {
                val state by customTextInputsViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CustomTextInputsShowcase(
                    state = state,
                    onFloatingLabelChange = customTextInputsViewModel::setFloatingLabelValue,
                    onSearchChange = customTextInputsViewModel::setSearchValue,
                    onOtpChange = customTextInputsViewModel::setOtpValue,
                    onPasswordChange = customTextInputsViewModel::setPassword,
                    onTogglePasswordVisible = customTextInputsViewModel::togglePasswordVisible,
                    onUnderlineChange = customTextInputsViewModel::setUnderlineValue
                )
            }
            spec.id == "image-custom-styles" -> {
                val state by customImagesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CustomImagesShowcase(
                    state = state,
                    onToggleZoom = customImagesViewModel::toggleZoomed,
                    onCycleAvatarStatus = customImagesViewModel::cycleAvatarStatus,
                    onComparisonFractionChange = customImagesViewModel::setComparisonFraction
                )
            }
            spec.id == "layout-custom-styles" -> {
                val state by layoutStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                LayoutStylesShowcase(
                    expandedAccordionIndex = state.expandedAccordionIndex,
                    onToggleAccordion = layoutStylesViewModel::toggleAccordion
                )
            }
            spec.id == "list-custom-styles" -> {
                val state by listStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                ListStylesShowcase(
                    swipeItems = state.swipeItems,
                    onDeleteSwipeItem = listStylesViewModel::deleteSwipeItem,
                    expandedRowId = state.expandedRowId,
                    onToggleExpandedRow = listStylesViewModel::toggleExpandedRow,
                    reorderItems = state.reorderItems,
                    onMoveReorderItem = listStylesViewModel::moveReorderItem
                )
            }
            spec.id == "map-custom-styles" -> {
                val state by mapStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                MapStylesShowcase(
                    state = state,
                    onTriggerPinDrop = mapStylesViewModel::triggerPinDrop,
                    onStyleChange = mapStylesViewModel::setSatelliteStyle
                )
            }
            spec.id == "media-custom-styles" -> {
                val state by mediaStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                MediaStylesShowcase(
                    state = state,
                    onToggleAudioPlayer = mediaStylesViewModel::toggleAudioPlayer,
                    onToggleMiniPlayer = mediaStylesViewModel::toggleMiniPlayer,
                    onScrubberProgressChanged = mediaStylesViewModel::onScrubberProgressChanged,
                    onQueueItemSelected = mediaStylesViewModel::onQueueItemSelected
                )
            }
            spec.id == "menu-custom-styles" -> {
                val state by menuStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                MenuStylesShowcase(
                    state = state,
                    onContextMenuSelectionChange = menuStylesViewModel::setContextMenuSelection,
                    onSegmentedSelectionChange = menuStylesViewModel::setSegmentedSelection
                )
            }
            spec.id == "nav-custom-styles" -> {
                val state by navStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                NavStylesShowcase(
                    morphingSelectedIndex = state.morphingSelectedIndex,
                    onSelectMorphing = navStylesViewModel::selectMorphing,
                    underlineSelectedIndex = state.underlineSelectedIndex,
                    onSelectUnderline = navStylesViewModel::selectUnderline,
                    segmentedSelectedIndex = state.segmentedSelectedIndex,
                    onSelectSegmented = navStylesViewModel::selectSegmented,
                    pillSelectedIndex = state.pillSelectedIndex,
                    onSelectPill = navStylesViewModel::selectPill,
                    railSelectedIndex = state.railSelectedIndex,
                    onSelectRail = navStylesViewModel::selectRail
                )
            }
            spec.id == "networking-custom-styles" -> {
                val state by networkingStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                NetworkingStylesShowcase(
                    state = state,
                    onToggleConnection = networkingStylesViewModel::toggleConnection,
                    onRetry = networkingStylesViewModel::retry,
                    onSimulateResponse = networkingStylesViewModel::simulateResponseArrive,
                    onSendRequest = networkingStylesViewModel::sendRequest,
                    onCycleSignal = networkingStylesViewModel::cycleSignal
                )
            }
            spec.id == "permission-custom-styles" -> {
                val state by permissionStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                PermissionStylesShowcase(
                    state = state,
                    onRationaleGrantedChange = permissionStylesViewModel::setRationaleGranted,
                    onStatusChipCycle = permissionStylesViewModel::cycleStatusChip,
                    onSettingsBannerToggle = permissionStylesViewModel::toggleSettingsBannerDismissed,
                    onChecklistToggle = permissionStylesViewModel::toggleChecklistItem,
                    onShieldGrantedChange = permissionStylesViewModel::setShieldPromptGranted
                )
            }
            spec.id == "progress-custom-styles" -> {
                val state by progressStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                ProgressStylesShowcase(
                    state = state,
                    onDottedStepNext = progressStylesViewModel::advanceDottedStep,
                    onDottedStepBack = progressStylesViewModel::retreatDottedStep,
                    onCircularIncrease = progressStylesViewModel::increaseCircularPercent,
                    onSegmentedAdvance = progressStylesViewModel::advanceSegmentedStep,
                    onSegmentedReset = progressStylesViewModel::resetSegmentedStep,
                    onWaveIncrease = progressStylesViewModel::increaseWavePercent
                )
            }
            spec.id == "storage-custom-styles" -> {
                val state by storageStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                StorageStylesShowcase(
                    state = state,
                    onStartUpload = storageStylesViewModel::startUpload,
                    onCancelUpload = storageStylesViewModel::cancelUpload,
                    onCycleSyncStatus = storageStylesViewModel::cycleSyncStatus,
                    onClearCache = storageStylesViewModel::clearCache
                )
            }
            spec.id == "animation-custom-styles" -> {
                val state by animationStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                AnimationStylesShowcase(
                    staggerReplayKey = state.staggerReplayKey,
                    onReplayStagger = animationStylesViewModel::replayStagger,
                    expandCardExpanded = state.expandCardExpanded,
                    onExpandCardExpandedChange = animationStylesViewModel::setExpandCardExpanded
                )
            }
            spec.id == "gesture-custom-styles" -> {
                val state by gestureStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                GestureStylesShowcase(
                    swipeCardDismissed = state.swipeCardDismissed,
                    onSwipeCardDismissedChange = gestureStylesViewModel::setSwipeCardDismissed,
                    pullToRefreshRefreshing = state.pullToRefreshRefreshing,
                    onTriggerPullToRefresh = gestureStylesViewModel::triggerPullToRefresh
                )
            }
            spec.id == "graphics-custom-styles" -> {
                val state by graphicsStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                GraphicsStylesShowcase(
                    progressRingPercent = state.progressRingPercent,
                    onIncreaseProgressRing = graphicsStylesViewModel::increaseProgressRing,
                    sparklineRedrawKey = state.sparklineRedrawKey,
                    onRedrawSparkline = graphicsStylesViewModel::redrawSparkline
                )
            }
            spec.id == "dialog-custom-styles" -> {
                val state by dialogStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                DialogStylesShowcase(state = state, viewModel = dialogStylesViewModel)
            }
            spec.id == "selection-custom-styles" -> {
                val state by selectionStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                SelectionStylesShowcase(state = state, viewModel = selectionStylesViewModel)
            }
            spec.id == "sensor-custom-styles" -> {
                val state by sensorStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                SensorStylesShowcase(state = state, viewModel = sensorStylesViewModel)
            }
            spec.id == "camera-custom-styles" -> {
                val state by cameraStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                CameraStylesShowcase(state = state, viewModel = cameraStylesViewModel)
            }
            spec.id == "slider-custom-styles" -> {
                val state by sliderStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                SliderStylesShowcase(state = state, viewModel = sliderStylesViewModel)
            }
            spec.id == "notification-custom-styles" -> {
                val state by notificationStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                NotificationStylesShowcase(state = state, viewModel = notificationStylesViewModel)
            }
            spec.id == "material-custom-styles" -> {
                val state by materialStylesViewModel.state.collectAsState()

                SectionHeader("Live Preview")
                MaterialStylesShowcase(state = state, viewModel = materialStylesViewModel)
            }
            GenericLivePreviewRegistry.previews.containsKey(spec.id) -> {
                // Self-contained preview composables (own local state) for categories
                // without a dedicated playground — no separate controls section needed.
                SectionHeader("Live Preview")
                GenericLivePreviewRegistry.previews[spec.id]?.invoke()
            }
        }

        // 4. Compose Code
        SectionHeader("Compose Code")
        CodeBlock(spec.composeCode)

        // 5. XML Code
        spec.xmlCode?.let { xml ->
            SectionHeader("XML Code")
            CodeBlock(xml)
        }

        // 6. Activity/ViewModel usage
        spec.viewModelUsage?.let { usage ->
            SectionHeader("Activity/ViewModel Usage")
            Text(usage)
        }

        // 7. Properties
        SectionHeader("Properties")
        PropertyTable(spec.properties)

        // 8. Events
        SectionHeader("Events")
        BulletList(spec.events)

        // 9. Best Practices
        SectionHeader("Best Practices")
        BulletList(spec.bestPractices)

        // 10. Common Mistakes
        SectionHeader("Common Mistakes")
        BulletList(spec.commonMistakes)

        // 11. Accessibility
        SectionHeader("Accessibility")
        BulletList(spec.accessibilityNotes)

        // 12. Performance Notes
        SectionHeader("Performance Notes")
        BulletList(spec.performanceNotes)

        // 13. Related Components
        SectionHeader("Related Components")
        spec.relatedComponentIds.forEach { relatedId ->
            AssistChip(onClick = { onRelatedComponentClick(relatedId) }, label = { Text(relatedId) })
        }
    }
}
