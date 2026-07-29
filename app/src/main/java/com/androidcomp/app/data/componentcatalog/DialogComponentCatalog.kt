package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object DialogComponentCatalog {

    private val alertDialog = ComponentSpec(
        id = "dialog-alert",
        category = ComponentCategory.DIALOGS,
        title = "Alert Dialog",
        overview = "A modal dialog that interrupts the user with a title, supporting text, " +
            "and confirm/dismiss actions — used for important decisions or confirmations.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Delete item?") },
                    text = { Text("This action cannot be undone.") },
                    confirmButton = {
                        TextButton(onClick = { onConfirmDelete(); showDialog = false }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                    }
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                MaterialAlertDialogBuilder(context)
                    .setTitle("Delete item?")
                    .setMessage("This action cannot be undone.")
                    .setPositiveButton("Delete") { _, _ -> onConfirmDelete() }
                    .setNegativeButton("Cancel", null)
                    .show()
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onDeleteConfirmed() {
                viewModel.deleteItem()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onDismissRequest", "() -> Unit", "required", "Called when the user taps outside the dialog or presses back."),
            ComponentProperty("confirmButton", "@Composable () -> Unit", "required", "Primary action slot, typically a TextButton."),
            ComponentProperty("dismissButton", "@Composable (() -> Unit)?", "null", "Secondary action slot for cancelling."),
            ComponentProperty("title", "@Composable (() -> Unit)?", "null", "Optional title content."),
            ComponentProperty("text", "@Composable (() -> Unit)?", "null", "Optional supporting body text.")
        ),
        events = listOf("onDismissRequest — fired on scrim tap or system back; buttons fire their own onClick lambdas."),
        bestPractices = listOf(
            "Reserve AlertDialog for decisions that truly need to interrupt the user — overuse causes dialog fatigue.",
            "Always provide a way to dismiss without acting (dismissButton or onDismissRequest) to avoid trapping the user."
        ),
        commonMistakes = listOf(
            "Leaving onDismissRequest empty, which prevents back/scrim-tap from closing the dialog.",
            "Putting more than two actions in a single AlertDialog, which should use a different pattern like a bottom sheet."
        ),
        accessibilityNotes = listOf(
            "Focus should move to the dialog when it appears so TalkBack announces the title first.",
            "Ensure the destructive action (e.g. Delete) is clearly labeled, not just color-coded, for color-blind users."
        ),
        performanceNotes = listOf(
            "Dialog content composition is cheap; avoid heavy state loading inside the dialog itself — load it before showing.",
            "Use a single Boolean/State flag to control visibility rather than recreating the dialog's state each time."
        ),
        relatedComponentIds = listOf("dialog-fullscreen"),
        minApi = 21
    )

    private val fullScreenDialog = ComponentSpec(
        id = "dialog-fullscreen",
        category = ComponentCategory.DIALOGS,
        title = "Full-Screen Dialog",
        overview = "A Dialog configured to fill the entire screen, used for complex flows " +
            "(e.g. multi-field forms) that need a modal context but more space than AlertDialog offers.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Dialog(
                    onDismissRequest = { showDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text("Edit Profile") },
                                    navigationIcon = {
                                        IconButton(onClick = { showDialog = false }) {
                                            Icon(Icons.Filled.Close, contentDescription = "Close")
                                        }
                                    }
                                )
                            }
                        ) { padding -> EditProfileForm(Modifier.padding(padding)) }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onProfileFormClosed() {
                viewModel.discardDraftIfUnsaved()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onDismissRequest", "() -> Unit", "required", "Called when the user requests to close the dialog."),
            ComponentProperty("properties", "DialogProperties", "DialogProperties()", "Controls width, dismiss-on-back, and dismiss-on-click-outside behavior."),
            ComponentProperty("content", "@Composable () -> Unit", "required", "The full-screen content shown inside the dialog window.")
        ),
        events = listOf("onDismissRequest — fired on system back press unless disabled via DialogProperties."),
        bestPractices = listOf(
            "Set usePlatformDefaultWidth = false so content can truly fill the screen instead of being constrained to dialog width.",
            "Provide an explicit close/back affordance in a top app bar since the dialog fills the screen and hides the host screen."
        ),
        commonMistakes = listOf(
            "Forgetting usePlatformDefaultWidth = false, leaving unwanted margins around the 'full-screen' content.",
            "Using a full-screen Dialog for simple confirmations where AlertDialog would be simpler and more consistent."
        ),
        accessibilityNotes = listOf(
            "Because it visually replaces the whole screen, treat it like a real screen: set a clear title announced by TalkBack.",
            "Ensure the close action has a minimum 48x48dp touch target, same as any other IconButton."
        ),
        performanceNotes = listOf(
            "Content inside is a full Compose subtree — avoid unnecessary recomposition by hoisting form state carefully.",
            "Prefer this over launching a whole new Activity when the flow is short-lived and returns to the same screen."
        ),
        relatedComponentIds = listOf("dialog-alert"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(alertDialog, fullScreenDialog)
}
