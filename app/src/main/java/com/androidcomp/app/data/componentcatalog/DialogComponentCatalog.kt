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

    private val dialogCustomStyles = ComponentSpec(
        id = "dialog-custom-styles",
        category = ComponentCategory.DIALOGS,
        title = "Custom Dialog Styles",
        overview = "Five fully custom-designed dialogs beyond the standard AlertDialog — a " +
            "bottom sheet action list, an icon-led confirmation dialog, a success celebration " +
            "dialog with a delayed checkmark reveal, an input dialog with a text field, and a " +
            "full-bleed image dialog. Tap the button below each one to see it open, and copy " +
            "its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun IconConfirmationDialog(visible: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit) {
                    if (visible) {
                        AlertDialog(
                            onDismissRequest = onDismiss,
                            icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                            title = { Text("Delete item?") },
                            text = { Text("This can't be undone.") },
                            confirmButton = { TextButton(onClick = onConfirm) { Text("Delete") } },
                            dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
                        )
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("visible", "Boolean", "required", "Controls whether each dialog is currently shown, hoisted in a single state class."),
            ComponentProperty("onShow / onDismiss", "() -> Unit", "required", "Toggle the corresponding visibility flag in the ViewModel."),
            ComponentProperty("inputText", "String", "\"\"", "Backing text for the input dialog's TextField, updated via onTextChange."),
            ComponentProperty("DialogProperties(usePlatformDefaultWidth = false)", "DialogProperties", "n/a", "Used by the full-bleed image dialog so its content can fill the screen width.")
        ),
        events = listOf("onShow/onDismiss/onSave/onConfirm — fired by each design's own buttons to drive its show/hide/commit state."),
        bestPractices = listOf(
            "Keep each dialog's visibility as an independent Boolean field on a single showcase state object rather than one shared 'currentDialog' enum, so multiple demos don't fight over one flag.",
            "Use `ModalBottomSheet` for action lists with more than 2-3 options instead of cramming them into an AlertDialog."
        ),
        commonMistakes = listOf(
            "Forgetting `DialogProperties(usePlatformDefaultWidth = false)` on the full-bleed image dialog, leaving unwanted margins around content meant to fill the screen.",
            "Not resetting input dialog text state on dismiss, so reopening it shows stale text from a previous, cancelled attempt."
        ),
        accessibilityNotes = listOf(
            "Custom Dialogs (not AlertDialog) don't get automatic title-announcement semantics — ensure the first focusable element is a clear heading for TalkBack.",
            "The success celebration dialog's delayed icon reveal should still be dismissible immediately; don't block the dismiss action behind the animation finishing."
        ),
        performanceNotes = listOf(
            "ModalBottomSheet content composes lazily as it slides in — avoid loading heavy data eagerly before the sheet is actually requested.",
            "Keep celebratory/animated dialogs' entrance animations short (under ~400ms) so they don't feel like they're blocking the user from proceeding."
        ),
        relatedComponentIds = listOf("dialog-alert", "dialog-fullscreen"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(alertDialog, fullScreenDialog, dialogCustomStyles)
}
