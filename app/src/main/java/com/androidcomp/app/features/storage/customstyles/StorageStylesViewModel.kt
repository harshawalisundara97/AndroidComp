package com.androidcomp.app.features.storage.customstyles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(StorageStylesState())
    val state: StateFlow<StorageStylesState> = _state.asStateFlow()

    fun startUpload() {
        if (_state.value.uploadState == UploadState.UPLOADING) return
        viewModelScope.launch {
            _state.value = _state.value.copy(uploadState = UploadState.UPLOADING, uploadProgress = 0f)
            var progress = 0f
            while (progress < 1f && _state.value.uploadState == UploadState.UPLOADING) {
                delay(120)
                progress = (progress + 0.05f).coerceAtMost(1f)
                _state.value = _state.value.copy(uploadProgress = progress)
            }
            if (_state.value.uploadState == UploadState.UPLOADING) {
                _state.value = _state.value.copy(uploadState = UploadState.DONE)
                delay(1000)
                _state.value = _state.value.copy(uploadState = UploadState.IDLE, uploadProgress = 0f)
            }
        }
    }

    fun cancelUpload() {
        _state.value = _state.value.copy(uploadState = UploadState.IDLE, uploadProgress = 0f)
    }

    fun cycleSyncStatus() {
        val next = when (_state.value.syncStatus) {
            SyncStatus.SYNCED -> SyncStatus.SYNCING
            SyncStatus.SYNCING -> SyncStatus.OFFLINE
            SyncStatus.OFFLINE -> SyncStatus.SYNCED
        }
        _state.value = _state.value.copy(syncStatus = next)
    }

    fun clearCache() {
        if (_state.value.isClearingCache) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isClearingCache = true)
            var size = _state.value.cacheSizeMb
            val startSize = size
            val steps = 20
            repeat(steps) {
                delay(40)
                size = (size - (startSize / steps).coerceAtLeast(1)).coerceAtLeast(0)
                _state.value = _state.value.copy(cacheSizeMb = size)
            }
            _state.value = _state.value.copy(cacheSizeMb = 0, isClearingCache = false)
        }
    }
}
