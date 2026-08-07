package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object PermissionComponentCatalog {

    private val requestPermission = ComponentSpec(
        id = "permission-request",
        category = ComponentCategory.PERMISSIONS,
        title = "Request Single Permission",
        overview = "The modern, non-deprecated way to request a single runtime permission " +
            "using the Activity Result API, replacing the old onRequestPermissionsResult " +
            "override.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        // proceed with camera capture
                    } else {
                        // show rationale or disable the feature
                    }
                }

                Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                    Text("Enable Camera")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <uses-permission android:name="android.permission.CAMERA" />
                </manifest>
            """.trimIndent()
        ),
        viewModelUsage = """
            private val _cameraGranted = MutableStateFlow(false)
            val cameraGranted: StateFlow<Boolean> = _cameraGranted.asStateFlow()

            fun onPermissionResult(granted: Boolean) {
                _cameraGranted.value = granted
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("contract", "ActivityResultContract<String, Boolean>", "ActivityResultContracts.RequestPermission()", "Defines the input/output contract for the launcher."),
            ComponentProperty("onResult", "(Boolean) -> Unit", "required", "Callback invoked with the grant result."),
            ComponentProperty("permission", "String", "required", "The permission string to request, e.g. Manifest.permission.CAMERA."),
            ComponentProperty("shouldShowRationale", "Boolean", "false", "Read via ActivityCompat.shouldShowRequestPermissionRationale to decide whether to explain before asking.")
        ),
        events = listOf("launcher.launch(permission) — triggers the system permission dialog.", "onResult — fired once the user responds to the dialog."),
        bestPractices = listOf(
            "Check ContextCompat.checkSelfPermission before launching — don't prompt if already granted.",
            "Show an in-app rationale before re-requesting a previously denied permission."
        ),
        commonMistakes = listOf(
            "Calling launcher.launch() unconditionally on every screen load, annoying users who already denied it.",
            "Registering the ActivityResultLauncher outside of composition setup or after the composable has already entered the composition, which throws an IllegalStateException."
        ),
        accessibilityNotes = listOf(
            "The system permission dialog itself is fully accessible by default (TalkBack-compatible); no extra work is required.",
            "Provide an accessible, descriptive rationale message rather than relying on icon-only prompts."
        ),
        performanceNotes = listOf(
            "Permission checks (checkSelfPermission) are cheap, synchronous binder-free calls on API 23+; safe to call on the main thread.",
            "Avoid re-registering launchers repeatedly; register once per Activity/Fragment/composable lifecycle."
        ),
        relatedComponentIds = listOf("permission-request-multiple", "permission-check-runtime"),
        minApi = 23
    )

    private val requestMultiplePermissions = ComponentSpec(
        id = "permission-request-multiple",
        category = ComponentCategory.PERMISSIONS,
        title = "Request Multiple Permissions",
        overview = "Requests a group of related runtime permissions in a single system dialog " +
            "flow, useful when a feature needs more than one permission (e.g. fine + coarse " +
            "location).",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { results: Map<String, Boolean> ->
                    val fineGranted = results[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                    val coarseGranted = results[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
                }

                Button(onClick = {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) {
                    Text("Enable Location")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
                    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
                </manifest>
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onLocationPermissionsResult(results: Map<String, Boolean>) {
                _locationGranted.value = results.values.any { it }
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("contract", "ActivityResultContract<Array<String>, Map<String, Boolean>>", "ActivityResultContracts.RequestMultiplePermissions()", "Contract for requesting an array of permissions at once."),
            ComponentProperty("permissions", "Array<String>", "required", "The set of permission strings to request together."),
            ComponentProperty("onResult", "(Map<String, Boolean>) -> Unit", "required", "Callback with a per-permission grant map."),
            ComponentProperty("POST_NOTIFICATIONS", "String", "n/a", "A notable API 33+ runtime permission often bundled with other requests on newer targets.")
        ),
        events = listOf("launcher.launch(permissions) — shows one or more system dialogs for the group.", "onResult — fired with the full grant map once all permissions are resolved."),
        bestPractices = listOf(
            "Group only permissions that belong to the same user-facing feature so the batch dialog makes sense contextually.",
            "Treat ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION as independent results — the user can grant only coarse."
        ),
        commonMistakes = listOf(
            "Assuming all requested permissions are granted together; each key in the result map must be checked individually.",
            "Forgetting that on Android 13+ (API 33), POST_NOTIFICATIONS is a runtime permission and must be requested explicitly, unlike on earlier versions."
        ),
        accessibilityNotes = listOf(
            "Sequential system dialogs are screen-reader friendly by default; do not attempt to build a custom multi-permission UI to \"improve\" this.",
            "Ensure any custom rationale screen shown before the batch request meets minimum touch target and contrast guidelines."
        ),
        performanceNotes = listOf(
            "Batching requests reduces the number of process/binder round-trips compared to sequential single-permission launches.",
            "The result map allocation is trivial; no meaningful performance concern at typical group sizes (2-4 permissions)."
        ),
        relatedComponentIds = listOf("permission-request", "permission-check-runtime"),
        minApi = 23
    )

    private val checkRuntimePermission = ComponentSpec(
        id = "permission-check-runtime",
        category = ComponentCategory.PERMISSIONS,
        title = "Check Runtime Permission Status",
        overview = "Synchronously checks whether a dangerous permission is currently granted, " +
            "used to gate UI or logic before deciding whether to launch a request flow.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val context = LocalContext.current
                val hasCameraPermission = remember {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                }

                if (hasCameraPermission) {
                    CameraPreview()
                } else {
                    Text("Camera permission required")
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun refreshCameraPermissionState(context: Context) {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                _cameraGranted.value = granted
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("context", "Context", "required", "Context used to resolve the app's current permission state."),
            ComponentProperty("permission", "String", "required", "The permission string being checked, e.g. Manifest.permission.CAMERA."),
            ComponentProperty("result", "Int", "n/a", "Returns PackageManager.PERMISSION_GRANTED or PERMISSION_DENIED.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Re-check permission state in onResume, since users can revoke permissions from system Settings at any time.",
            "Prefer ContextCompat.checkSelfPermission over the raw Context.checkPermission for consistent pre-API-23 behavior."
        ),
        commonMistakes = listOf(
            "Checking permission state once at app startup and caching it indefinitely without re-checking on resume.",
            "Confusing PERMISSION_GRANTED (an Int constant, value 0) with a Boolean, leading to incorrect comparisons."
        ),
        accessibilityNotes = listOf(
            "When permission is denied, surface a clear, non-icon-only textual explanation of what is disabled and why.",
            "Ensure the fallback UI shown when permission is denied is fully navigable via screen reader, not just visually indicated."
        ),
        performanceNotes = listOf(
            "checkSelfPermission is a fast local lookup (cached by the OS) and safe to call frequently, including in onResume.",
            "Avoid calling it inside a tight recomposition loop without remembering the result, to prevent redundant checks."
        ),
        relatedComponentIds = listOf("permission-request", "permission-request-multiple"),
        minApi = 23
    )

    private val permissionCustomStyles = ComponentSpec(
        id = "permission-custom-styles",
        category = ComponentCategory.PERMISSIONS,
        title = "Custom Permission UI Styles",
        overview = "Five real-world, hand-styled permission UI patterns — a rationale card, " +
            "a status chip, a settings-redirect banner, a permission checklist, and a pulsing " +
            "shield prompt — built as pure mock UI with genuine Compose animations and no real " +
            "runtime permission calls.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val borderColor by animateColorAsState(
                    if (granted) Color(0xFF2ECC71) else Color(0xFFF2F2F2)
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    AnimatedContent(granted) { isGranted ->
                        if (isGranted) {
                            // checkmark + \"Access Granted\" row
                        } else {
                            // icon + rationale text + Allow / Not now buttons
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("rationaleGranted", "Boolean", "false", "Drives the Rationale Card's animated transition into its Granted state."),
            ComponentProperty("statusChipState", "PermissionGrantState", "NOT_REQUESTED", "Tri-state enum cycled by tapping the Status Chip."),
            ComponentProperty("settingsBannerDismissed", "Boolean", "false", "Controls the AnimatedVisibility of the Settings Redirect Banner."),
            ComponentProperty("checklist", "List<ChecklistPermission>", "4 mock permissions", "Backs the Permission Checklist; each row toggles independently."),
            ComponentProperty("shieldPromptGranted", "Boolean", "false", "Stops the infinite pulse animation and swaps the shield icon once granted.")
        ),
        events = listOf(
            "onAllow / onNotNow — Rationale Card button taps.",
            "onClick — Status Chip tap, cycles Not requested -> Granted -> Denied.",
            "onOpenSettings — Settings Redirect Banner button tap (mock only, no real Settings intent).",
            "onToggle(label) — Permission Checklist row tap.",
            "onAllow — Animated Shield Prompt button tap."
        ),
        bestPractices = listOf(
            "Keep these patterns as pure UI mockups when illustrating design language; wire them to real ActivityResultContracts calls only at the screen/ViewModel layer.",
            "Use animateColorAsState / AnimatedContent for state transitions so denial and grant feel intentional rather than abrupt."
        ),
        commonMistakes = listOf(
            "Treating a mock \"Open Settings\" banner as a substitute for a real Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS) call in production code.",
            "Forgetting to re-check real permission state in onResume after a user returns from system Settings, when adapting this pattern to production."
        ),
        accessibilityNotes = listOf(
            "Status Chip and checklist rows must expose their granted/denied state via contentDescription or semantics, not color alone.",
            "All tap targets (chip, banner button, checklist rows) should meet the 48x48dp minimum touch target guideline."
        ),
        performanceNotes = listOf(
            "rememberInfiniteTransition in the Shield Prompt keeps animating while composed; stop or hide it once granted to avoid unnecessary recomposition.",
            "AnimatedContent/AnimatedVisibility allocate lightweight transition state; safe to use per-row in the checklist without notable overhead."
        ),
        relatedComponentIds = listOf("permission-request", "permission-request-multiple", "permission-check-runtime"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(requestPermission, requestMultiplePermissions, checkRuntimePermission, permissionCustomStyles)
}
