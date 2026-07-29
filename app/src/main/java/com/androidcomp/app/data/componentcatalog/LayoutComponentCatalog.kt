package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object LayoutComponentCatalog {

    private val weightedRow = ComponentSpec(
        id = "layout-row-weight",
        category = ComponentCategory.LAYOUTS,
        title = "Row with Weighted Children",
        overview = "Arranges children horizontally, using `Modifier.weight` to proportionally " +
            "distribute remaining space — the Compose equivalent of a LinearLayout with " +
            "layout_weight.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Label", modifier = Modifier.weight(1f))
                    Text("Value", modifier = Modifier.weight(2f))
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">
                    <TextView
                        android:layout_width="0dp"
                        android:layout_weight="1"
                        android:layout_height="wrap_content"
                        android:text="Label" />
                    <TextView
                        android:layout_width="0dp"
                        android:layout_weight="2"
                        android:layout_height="wrap_content"
                        android:text="Value" />
                </LinearLayout>
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("horizontalArrangement", "Arrangement.Horizontal", "Arrangement.Start", "How children are spaced along the main axis."),
            ComponentProperty("verticalAlignment", "Alignment.Vertical", "Alignment.Top", "How children are aligned along the cross axis."),
            ComponentProperty("modifier", "Modifier", "Modifier", "Modifier applied to the Row itself, e.g. fillMaxWidth()."),
            ComponentProperty("content", "@Composable RowScope.() -> Unit", "required", "Children; only usable inside RowScope can call Modifier.weight().")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use Arrangement.spacedBy() instead of manual Spacer composables between children for consistent gaps.",
            "Reserve Modifier.weight() for children that should share remaining space proportionally; use wrapContent otherwise."
        ),
        commonMistakes = listOf(
            "Calling Modifier.weight() outside a RowScope/ColumnScope receiver — it only compiles inside those scopes.",
            "Combining fillMaxWidth() on a weighted child with another fillMaxWidth() sibling, causing measurement conflicts."
        ),
        accessibilityNotes = listOf(
            "Ensure reading order in a Row matches visual left-to-right order, or set a custom traversal order for RTL locales.",
            "Row does not clip overflow by default — clipped weighted text should use Modifier.weight(1f, fill = true) with ellipsis."
        ),
        performanceNotes = listOf(
            "Row/Column are lightweight single-pass layouts; prefer them over ConstraintLayout for simple linear arrangements."
        ),
        relatedComponentIds = listOf("layout-box-stack"),
        minApi = 21
    )

    private val boxStack = ComponentSpec(
        id = "layout-box-stack",
        category = ComponentCategory.LAYOUTS,
        title = "Box with Layering",
        overview = "Stacks children on top of one another along the z-axis, the Compose " +
            "equivalent of a FrameLayout, commonly used for badges, overlays, and image captions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Box(modifier = Modifier.size(120.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "New",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <FrameLayout
                    android:layout_width="120dp"
                    android:layout_height="120dp">
                    <ImageView
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:scaleType="centerCrop"
                        android:src="@drawable/banner" />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="top|end"
                        android:padding="8dp"
                        android:text="New" />
                </FrameLayout>
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("contentAlignment", "Alignment", "Alignment.TopStart", "Default alignment applied to children that don't specify their own align()."),
            ComponentProperty("propagateMinConstraints", "Boolean", "false", "Whether Box's incoming min constraints are passed to its children."),
            ComponentProperty("modifier", "Modifier", "Modifier", "Modifier applied to the Box itself, e.g. size()."),
            ComponentProperty("content", "@Composable BoxScope.() -> Unit", "required", "Children; BoxScope provides Modifier.align() and matchParentSize().")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use Modifier.align() per child inside BoxScope rather than nesting extra Rows/Columns to position overlays.",
            "Use matchParentSize() (not fillMaxSize()) for a child that must size itself to Box's other content, e.g. a background image."
        ),
        commonMistakes = listOf(
            "Relying on child declaration order for stacking without realizing later children always draw on top.",
            "Using Box purely for padding/centering a single child when a simpler Modifier chain would suffice."
        ),
        accessibilityNotes = listOf(
            "Overlapping text and image content must maintain contrast; consider a scrim behind text on top of images.",
            "Ensure touch targets of overlapping interactive children don't unintentionally overlap each other."
        ),
        performanceNotes = listOf(
            "Box performs a single measurement pass per child; avoid deeply nested Box-in-Box hierarchies for complex overlays."
        ),
        relatedComponentIds = listOf("layout-row-weight"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(weightedRow, boxStack)
}
