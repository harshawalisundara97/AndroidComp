package com.androidcomp.app.features.permissions.customstyles

enum class PermissionGrantState { NOT_REQUESTED, GRANTED, DENIED }

data class ChecklistPermission(
    val label: String,
    val granted: Boolean
)

data class PermissionStylesState(
    val rationaleGranted: Boolean = false,
    val statusChipState: PermissionGrantState = PermissionGrantState.NOT_REQUESTED,
    val settingsBannerDismissed: Boolean = false,
    val checklist: List<ChecklistPermission> = listOf(
        ChecklistPermission("Camera", granted = true),
        ChecklistPermission("Location", granted = false),
        ChecklistPermission("Microphone", granted = false),
        ChecklistPermission("Notifications", granted = true)
    ),
    val shieldPromptGranted: Boolean = false
)
