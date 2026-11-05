package com.hassaku.plamotranslate.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetectedTextBlock(
    val id: Int,
    val text: String,
    // Normalized bounding box 0f..1f for overlay (left, top, right, bottom)
    val bounds: FloatArray = floatArrayOf(0f, 0f, 0f, 0f)
)

data class CameraUiState(
    val hasPermission: Boolean = false,
    val isAnalyzing: Boolean = false,
    val detectedBlocks: List<DetectedTextBlock> = emptyList(),
    val selectedText: String? = null,
    val error: String? = null
)

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasPermission = granted) }
    }

    fun onTextDetected(blocks: List<DetectedTextBlock>) {
        _uiState.update {
            it.copy(detectedBlocks = blocks, isAnalyzing = false)
        }
    }

    fun selectBlock(block: DetectedTextBlock) {
        _uiState.update { it.copy(selectedText = block.text) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedText = null) }
    }

    fun setAnalyzing(analyzing: Boolean) {
        _uiState.update { it.copy(isAnalyzing = analyzing) }
    }

    fun setError(message: String?) {
        _uiState.update { it.copy(error = message) }
    }
}
