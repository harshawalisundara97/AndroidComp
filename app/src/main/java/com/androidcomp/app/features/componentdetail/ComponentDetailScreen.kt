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
import com.androidcomp.app.features.buttons.playground.PlaygroundState
import com.androidcomp.app.features.buttons.playground.PlaygroundViewModel
import com.androidcomp.app.features.buttons.preview.ButtonPreviewRegistry

@Composable
fun ComponentDetailScreen(
    onRelatedComponentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    detailViewModel: ComponentDetailViewModel = hiltViewModel(),
    playgroundViewModel: PlaygroundViewModel = hiltViewModel()
) {
    val spec by detailViewModel.spec.collectAsState()
    val playgroundState by playgroundViewModel.state.collectAsState()

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
                    playgroundState,
                    playgroundViewModel,
                    onRelatedComponentClick
                )
            }
        }
    }
}

@Composable
private fun ComponentDetailContent(
    spec: ComponentSpec,
    playgroundState: PlaygroundState,
    playgroundViewModel: PlaygroundViewModel,
    onRelatedComponentClick: (String) -> Unit
) {
    Column {

        // 1. Overview
        SectionHeader("Overview")
        Text(spec.overview)

        // 2. Live Preview
        SectionHeader("Live Preview")
        ButtonPreviewRegistry.previews[spec.id]?.invoke(playgroundState)

        // 3. Interactive Playground
        SectionHeader("Interactive Playground")
        PlaygroundControls(
            state = playgroundState,
            onLabelChange = playgroundViewModel::setLabel,
            onEnabledChange = playgroundViewModel::setEnabled
        )

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
