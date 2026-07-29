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

    val all: List<ComponentSpec> = listOf(previewView, imageCapture)
}
