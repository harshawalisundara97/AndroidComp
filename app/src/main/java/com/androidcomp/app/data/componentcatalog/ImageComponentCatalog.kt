package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object ImageComponentCatalog {

    private val localImage = ComponentSpec(
        id = "image-painter-resource",
        category = ComponentCategory.IMAGES,
        title = "Image (painterResource)",
        overview = "Displays a bitmap or vector drawable bundled in the app's resources, the " +
            "standard way to show local static images in Compose.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App logo",
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <ImageView
                    android:layout_width="96dp"
                    android:layout_height="96dp"
                    android:src="@drawable/logo"
                    android:contentDescription="@string/app_logo_description"
                    android:scaleType="fitCenter" />
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("painter", "Painter", "required", "The image source, e.g. painterResource(id = ...)."),
            ComponentProperty("contentDescription", "String?", "required", "Accessibility description; pass null only for purely decorative images."),
            ComponentProperty("contentScale", "ContentScale", "ContentScale.Fit", "How the image is scaled/cropped within its bounds."),
            ComponentProperty("alignment", "Alignment", "Alignment.Center", "Alignment of the image within its bounds after scaling."),
            ComponentProperty("alpha", "Float", "DefaultAlpha", "Opacity applied to the image, from 0f to 1f.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use vector drawables (ImageVector/XML VectorDrawable) for icons and simple art to stay resolution-independent.",
            "Set an explicit Modifier.size() or aspect ratio to prevent layout jumps while the image loads/measures."
        ),
        commonMistakes = listOf(
            "Passing contentDescription = null for meaningful images, hiding them from screen reader users entirely.",
            "Forgetting ContentScale.Crop when filling a fixed-size container, leading to unexpected letterboxing."
        ),
        accessibilityNotes = listOf(
            "contentDescription should describe the image's purpose, not restate 'image of' or the filename.",
            "Purely decorative images (e.g. background textures) should explicitly pass contentDescription = null."
        ),
        performanceNotes = listOf(
            "Large bitmap drawables should be downsampled to their display size to avoid excessive memory use.",
            "Prefer vector drawables over large PNGs for simple graphics to reduce APK size and memory footprint."
        ),
        relatedComponentIds = listOf("image-async"),
        minApi = 21
    )

    private val asyncImage = ComponentSpec(
        id = "image-async",
        category = ComponentCategory.IMAGES,
        title = "AsyncImage (Coil)",
        overview = "Loads and displays a remote or content-provider image asynchronously using " +
            "Coil, with built-in memory/disk caching and placeholder/error states.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.avatar_placeholder),
                    error = painterResource(id = R.drawable.avatar_error)
                )
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            val profileImageUrl: StateFlow<String?> = userRepository.observeAvatarUrl()
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("model", "Any?", "required", "The image data source: URL, Uri, File, or resource id."),
            ComponentProperty("contentDescription", "String?", "required", "Accessibility description of the loaded image."),
            ComponentProperty("placeholder", "Painter?", "null", "Shown while the image is loading."),
            ComponentProperty("error", "Painter?", "null", "Shown if the image request fails."),
            ComponentProperty("contentScale", "ContentScale", "ContentScale.Fit", "How the loaded image is scaled/cropped within its bounds.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Always supply `placeholder` and `error` painters to avoid a jarring blank space during load or on failure.",
            "Reuse a single shared ImageLoader (via Hilt or a CompositionLocal) rather than creating one per call."
        ),
        commonMistakes = listOf(
            "Loading full-resolution images into small thumbnails without letting Coil size/downsample via the modifier size.",
            "Not handling the null/loading state, causing layout to jump once the image resolves."
        ),
        accessibilityNotes = listOf(
            "contentDescription should be set even though the image loads asynchronously — TalkBack needs it immediately.",
            "If the same image is decorative in context, pass contentDescription = null explicitly rather than a generic label."
        ),
        performanceNotes = listOf(
            "Coil automatically downsamples to the target composable's constrained size — always constrain size via Modifier.",
            "Rely on Coil's default memory+disk cache rather than implementing custom caching for network images."
        ),
        relatedComponentIds = listOf("image-painter-resource"),
        minApi = 21
    )

    private val imageCustomStyles = ComponentSpec(
        id = "image-custom-styles",
        category = ComponentCategory.IMAGES,
        title = "Custom Image Treatments",
        overview = "Five common image UI patterns built on placeholders — a shimmer loading " +
            "sweep, a tap-to-zoom viewer, an avatar with an animated status dot, a gradient " +
            "scrim with a caption overlay, and a drag-controlled before/after comparison " +
            "slider. Interact with each below and copy its Compose code.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun ShimmerLoadingPlaceholder() {
                    val transition = rememberInfiniteTransition()
                    val shimmerOffset by transition.animateFloat(
                        initialValue = -1f,
                        targetValue = 2f,
                        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing))
                    )

                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFE9EAEE), Color(0xFFF6F7F9), Color(0xFFE9EAEE)),
                                    start = Offset(shimmerOffset * 300f, 0f),
                                    end = Offset(shimmerOffset * 300f + 300f, 300f)
                                )
                            )
                    )
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("rememberInfiniteTransition()", "InfiniteTransition", "n/a", "Drives the shimmer's continuously looping gradient sweep offset."),
            ComponentProperty("graphicsLayer { scaleX/scaleY }", "Float", "1f", "Scales the tap-to-zoom placeholder on the compositor without a layout pass."),
            ComponentProperty("detectHorizontalDragGestures", "PointerInputScope extension", "n/a", "Drives the before/after slider's clip fraction from real horizontal drag deltas."),
            ComponentProperty("Brush.verticalGradient", "Brush", "n/a", "Produces the bottom scrim so the overlaid caption stays legible on any image.")
        ),
        events = listOf("onToggle / onTap — fired to toggle zoom or cycle the avatar status.", "onFractionChange — fired continuously while dragging the comparison slider."),
        bestPractices = listOf(
            "Constrain shimmer and other infinite animations to only run while their content is actually loading, not indefinitely once real content is shown.",
            "Coerce the comparison slider's fraction to [0f, 1f] so a fast drag can't push the handle past the image bounds."
        ),
        commonMistakes = listOf(
            "Leaving an infiniteRepeatable shimmer running behind content that has already loaded, wasting battery and compositor work.",
            "Building the before/after slider by measuring pixel widths only once instead of reading live constraints (BoxWithConstraints), which breaks on rotation or resizing."
        ),
        accessibilityNotes = listOf(
            "The status dot's color change alone isn't enough to convey status — pair with a contentDescription or a label users can query via TalkBack.",
            "A drag-only comparison slider should also support tapping to jump the divider to that position, so it isn't drag-only for motor-impaired users."
        ),
        performanceNotes = listOf(
            "Prefer graphicsLayer over animating layout-affecting properties (size/padding) for the zoom effect to avoid extra measure passes.",
            "The shimmer gradient's Brush should be recreated only from remembered/animated values, not rebuilt with new Offset objects that break animation smoothness."
        ),
        relatedComponentIds = listOf("image-painter-resource", "image-async"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(localImage, asyncImage, imageCustomStyles)
}
