package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object CameraComponentCatalog {

    private val previewView = ComponentSpec(
        id = "camera-preview",
        category = ComponentCategory.CAMERA,
        title = "CameraX PreviewView",
        overview = "A `View`-based live camera viewfinder from CameraX, embedded into Compose " +
            "via `AndroidView` interop, bound to a lifecycle-aware `ProcessCameraProvider` for " +
            "showing a real-time camera feed.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun CameraPreview(modifier: Modifier = Modifier) {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val context = LocalContext.current

                    AndroidView(
                        modifier = modifier,
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview
                                )
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <androidx.camera.view.PreviewView
                    android:id="@+id/previewView"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onCameraPermissionGranted() {
                _cameraReady.value = true
            }
            // ViewModel exposes readiness/state; the actual PreviewView/ProcessCameraProvider
            // lifecycle stays in the composable since it's tied to LocalLifecycleOwner.
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("scaleType", "PreviewView.ScaleType", "FILL_CENTER", "How the camera stream is scaled/cropped within the view bounds."),
            ComponentProperty("implementationMode", "PreviewView.ImplementationMode", "PERFORMANCE", "Whether to use a SurfaceView-backed or TextureView-backed implementation."),
            ComponentProperty("cameraSelector", "CameraSelector", "DEFAULT_BACK_CAMERA", "Chooses front vs. back physical camera to bind."),
            ComponentProperty("surfaceProvider", "Preview.SurfaceProvider", "previewView.surfaceProvider", "Bridges the CameraX Preview use case's output to this view's surface.")
        ),
        events = listOf("OnStreamStateChanged — reported via previewView.previewStreamState observable LiveData when the stream starts/stops."),
        bestPractices = listOf(
            "Request and verify the `android.permission.CAMERA` runtime permission before binding any camera use case.",
            "Always call `cameraProvider.unbindAll()` before rebinding use cases to avoid `IllegalStateException` from conflicting bindings."
        ),
        commonMistakes = listOf(
            "Binding the camera provider without tying it to a lifecycle owner, causing the camera to stay open after the screen is destroyed and draining battery.",
            "Forgetting to declare the `<uses-feature android:name=\"android.hardware.camera\">` and CAMERA permission in the manifest, causing a runtime crash on bind."
        ),
        accessibilityNotes = listOf(
            "Provide a non-visual affordance (e.g. a labeled capture button with contentDescription) since the live viewfinder itself conveys no accessible semantics.",
            "Ensure capture/shutter controls meet the 48x48dp minimum touch target even when overlaid on the camera feed."
        ),
        performanceNotes = listOf(
            "Use `ImplementationMode.PERFORMANCE` (SurfaceView-backed) for lower latency and power use versus `COMPATIBLE` (TextureView), unless overlay animations require TextureView.",
            "Unbind use cases (`unbindAll()`) when navigating away to release the camera hardware promptly for other apps."
        ),
        relatedComponentIds = listOf("camera-imagecapture"),
        minApi = 21
    )

    private val imageCapture = ComponentSpec(
        id = "camera-imagecapture",
        category = ComponentCategory.CAMERA,
        title = "CameraX ImageCapture",
        overview = "A CameraX use case bound alongside Preview that captures a high-resolution " +
            "still photo to a file or in-memory buffer, with configurable flash and capture mode.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val imageCapture = remember {
                    ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                }

                // bound together with Preview during ProcessCameraProvider.bindToLifecycle(...)

                fun takePhoto(context: Context, imageCapture: ImageCapture) {
                    val photoFile = File(context.filesDir, "photo_\${'$'}{System.currentTimeMillis()}.jpg")
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                // photoFile now contains the captured JPEG
                            }
                            override fun onError(exception: ImageCaptureException) {
                                // handle capture failure
                            }
                        }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onShutterPressed() {
                viewModelScope.launch {
                    val result = repository.capturePhoto()
                    _lastPhotoUri.value = result
                }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("captureMode", "Int", "CAPTURE_MODE_MINIMIZE_LATENCY", "Trades off shutter latency vs. image quality (MINIMIZE_LATENCY vs. MAXIMIZE_QUALITY)."),
            ComponentProperty("flashMode", "Int", "FLASH_MODE_OFF", "Controls flash behavior: OFF, ON, or AUTO."),
            ComponentProperty("targetRotation", "Int", "display.rotation", "Rotation applied to the output image to match device orientation."),
            ComponentProperty("jpegQuality", "Int", "95", "Compression quality (1-100) used when saving as JPEG.")
        ),
        events = listOf(
            "onImageSaved(OutputFileResults) — fired when the file has been written successfully.",
            "onError(ImageCaptureException) — fired when capture or file I/O fails."
        ),
        bestPractices = listOf(
            "Check/request the `CAMERA` permission (and `WRITE_EXTERNAL_STORAGE` only on legacy API levels below 29) before invoking takePicture.",
            "Run takePicture callbacks on a main or dedicated executor and move any heavy post-processing off the main thread."
        ),
        commonMistakes = listOf(
            "Calling takePicture before the use case is actually bound and the camera session is active, resulting in a silent no-op or exception.",
            "Ignoring the onError callback, leaving the UI stuck in a 'capturing' state when a capture genuinely fails."
        ),
        accessibilityNotes = listOf(
            "Announce capture success/failure via an accessibility live region or Snackbar since a photo being saved has no inherent visual change for screen reader users.",
            "Ensure the shutter button has a clear contentDescription such as \"Take photo\"."
        ),
        performanceNotes = listOf(
            "CAPTURE_MODE_MINIMIZE_LATENCY reduces shutter lag at a small quality cost — prefer it for casual/social capture flows.",
            "Writing large JPEGs to disk on the main thread's callback executor can jank the UI; use `Dispatchers.IO`-backed executors for the save step where possible."
        ),
        relatedComponentIds = listOf("camera-preview"),
        minApi = 21
    )

    private val cameraCustomStyles = ComponentSpec(
        id = "camera-custom-styles",
        category = ComponentCategory.CAMERA,
        title = "Custom Camera Control Styles",
        overview = "Five fully custom-designed camera control UI patterns — a capture button with an " +
            "expanding press ring, a rule-of-thirds viewfinder grid overlay, a cycling flash mode " +
            "toggle, a sliding Photo/Video segmented switcher, and an animated 3-2-1 shutter " +
            "countdown. This is a UI-mockup-only showcase: each design draws a dark placeholder Box " +
            "standing in for a live camera feed, with real interactive chrome/controls layered on " +
            "top — there is no real CameraX wiring or camera permission request here, so these " +
            "composables can be reused as-is and simply layered over a real `PreviewView` later.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun CaptureButtonWithPressRing(pressed: Boolean, onPressedChange: (Boolean) -> Unit) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    val ringScale by animateFloatAsState(if (isPressed) 1.7f else 1f, tween(350))
                    val ringAlpha by animateFloatAsState(if (isPressed) 0f else 0.55f, tween(350))
                    val buttonScale by animateFloatAsState(
                        if (isPressed) 0.88f else 1f,
                        spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )

                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.size(66.dp * ringScale).border(2.dp, Color.White.copy(alpha = ringAlpha), CircleShape))
                        Box(
                            Modifier
                                .size(66.dp)
                                .border(3.dp, Color.White, CircleShape)
                                .clickable(interactionSource, indication = null) { onPressedChange(!pressed) }
                        ) {
                            Box(Modifier.size(48.dp * buttonScale).background(Color.White, CircleShape))
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("pressed / gridVisible / flashMode / captureMode", "Boolean / Boolean / Int / Int", "false / true / 0 / 0", "Simple hoisted state per design — press state, grid visibility, 3-state flash mode, and 2-state photo/video mode."),
            ComponentProperty("countdownValue", "Int?", "null", "Current 3-2-1 countdown tick for the shutter countdown design, driven by a coroutine `delay` loop in the ViewModel."),
            ComponentProperty("justCaptured", "Boolean", "false", "Briefly true right after the countdown finishes, to show a checkmark 'capture' flash before resetting."),
            ComponentProperty("interactionSource", "MutableInteractionSource", "remember { MutableInteractionSource() }", "Feeds `collectIsPressedAsState()` so the press-ring button can react to press state directly, independent of a click callback.")
        ),
        events = listOf(
            "onPressedChange(Boolean) — fired when the shutter button is tapped.",
            "onToggle() — fired when the viewfinder grid overlay is tapped to show/hide the grid.",
            "onCycle() — fired when the flash mode control is tapped, advancing Off -> Auto -> On -> Off.",
            "onModeChange(Int) — fired when Photo or Video is tapped on the segmented switcher.",
            "onStart() — fired when the countdown timer icon is tapped, kicking off the 3-2-1 sequence."
        ),
        bestPractices = listOf(
            "Keep these as pure UI-mockup composables decoupled from CameraX — layer them visually over a real `PreviewView`/`AndroidView` rather than baking capture logic into them.",
            "Drive the press ring and mode-switcher indicator from animated state (`animateFloatAsState`/`animateDpAsState`) rather than snapping instantly, so the chrome feels native.",
            "Disable the countdown control's clickable while a countdown is already running to avoid overlapping coroutine loops.",
            "Use `MutableInteractionSource`/`collectIsPressedAsState` instead of a manual `onPress`/`onRelease` pointer handler for standard press-state animations."
        ),
        commonMistakes = listOf(
            "Wiring these mockups directly to `ImageCapture.takePicture()` calls, which conflates UI styling work with real capture logic and permission handling.",
            "Forgetting to cancel/guard the countdown coroutine, letting a second tap start a second overlapping countdown loop.",
            "Hardcoding the segmented switcher's slide distance instead of deriving it from the same `segmentWidth` used to lay out both segments, causing indicator drift if sizes change.",
            "Using `clickable` without `indication = null` on the press-ring button, causing a double visual feedback (default ripple plus the custom ring animation)."
        ),
        accessibilityNotes = listOf(
            "Give the shutter button a `contentDescription` of \"Take photo\" or \"Take video\" depending on the active mode.",
            "Announce flash mode changes (\"Flash: Auto\") via `contentDescription` on the icon since the crossfade alone isn't conveyed to TalkBack.",
            "Ensure the Photo/Video segmented switcher's tap targets are each at least 48x48dp even though the visual pill is narrower.",
            "For the countdown, consider an accessibility live region announcement (\"3… 2… 1… Captured\") since the large animated number is a purely visual cue."
        ),
        performanceNotes = listOf(
            "The countdown's `delay()`-based coroutine loop only recomposes the small `Text`/`AnimatedContent` region, not the whole showcase — keep state scoped that way.",
            "Prefer `Canvas`/`drawLine` for the grid overlay over stacking multiple thin `Box` dividers, which is cheaper to draw and animate.",
            "`animateDpAsState`/`animateFloatAsState` allocate a single `Animatable` under the hood; avoid recreating these composables unnecessarily inside frequently-recomposing lists."
        ),
        relatedComponentIds = listOf("camera-preview", "camera-imagecapture"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(previewView, imageCapture, cameraCustomStyles)
}
