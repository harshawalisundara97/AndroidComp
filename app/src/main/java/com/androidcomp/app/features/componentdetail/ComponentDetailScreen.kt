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

@Composable
fun ComponentDetailScreen(
    onRelatedComponentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    detailViewModel: ComponentDetailViewModel = hiltViewModel(),
    buttonPlaygroundViewModel: PlaygroundViewModel = hiltViewModel(),
    textPlaygroundViewModel: TextPlaygroundViewModel = hiltViewModel(),
    selectionPlaygroundViewModel: SelectionPlaygroundViewModel = hiltViewModel()
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
