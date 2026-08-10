package com.androidcomp.app.features.storage.customstyles

enum class UploadState { IDLE, UPLOADING, DONE }

enum class SyncStatus { SYNCED, SYNCING, OFFLINE }

data class StorageStylesState(
    val uploadState: UploadState = UploadState.IDLE,
    val uploadProgress: Float = 0f,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val cacheSizeMb: Int = 245,
    val isClearingCache: Boolean = false
)
