package com.androidcomp.app.domain.model

data class ComponentSpec(
    val id: String,
    val category: ComponentCategory,
    val title: String,
    val overview: String,
    val composeCode: CodeSample,
    val xmlCode: CodeSample?,
    val viewModelUsage: String?,
    val properties: List<ComponentProperty>,
    val events: List<String>,
    val bestPractices: List<String>,
    val commonMistakes: List<String>,
    val accessibilityNotes: List<String>,
    val performanceNotes: List<String>,
    val relatedComponentIds: List<String>,
    val minApi: Int
)
