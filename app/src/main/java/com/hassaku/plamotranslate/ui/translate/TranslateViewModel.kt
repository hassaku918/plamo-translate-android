package com.hassaku.plamotranslate.ui.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassaku.plamotranslate.data.inference.TranslationEngine
import com.hassaku.plamotranslate.data.model.ModelManager
import com.hassaku.plamotranslate.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TranslateUiState(
    val sourceText: String = "",
    val translatedText: String = "",
    val sourceLang: String = "English",
    val targetLang: String = "Japanese",
    val isTranslating: Boolean = false,
    val error: String? = null,
    val style: TranslationStyle = TranslationStyle.NORMAL,
    val isModelReady: Boolean = false,
    val statusHint: String = "設定からモデルをダウンロード＆ロードしてください"
)

enum class TranslationStyle(val label: String) {
    NORMAL("通常"),
    POLITE("敬体"),
    CASUAL("常体")
}

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translationEngine: TranslationEngine,
    private val historyRepository: HistoryRepository,
    private val modelManager: ModelManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            modelManager.refreshStatus()
            val path = modelManager.getModelPath()
            if (path != null && !translationEngine.isModelLoaded) {
                _uiState.update { it.copy(statusHint = "モデルをロード中…") }
                val result = translationEngine.loadModel(path)
                modelManager.markLoaded(result.isSuccess)
                _uiState.update {
                    it.copy(
                        isModelReady = result.isSuccess,
                        statusHint = if (result.isSuccess) {
                            "本推論準備完了（PLaMo GGUF）"
                        } else {
                            result.exceptionOrNull()?.message
                                ?: "ロード失敗。設定から再試行してください"
                        }
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isModelReady = translationEngine.isModelLoaded,
                        statusHint = if (translationEngine.isModelLoaded) {
                            "本推論準備完了"
                        } else if (path != null) {
                            "モデルはあります。設定からロードしてください"
                        } else {
                            "設定 → GGUFダウンロード → ロード"
                        }
                    )
                }
            }
        }
    }

    fun onSourceTextChange(text: String) {
        _uiState.update { it.copy(sourceText = text, error = null) }
    }

    fun swapLanguages() {
        _uiState.update {
            it.copy(
                sourceLang = it.targetLang,
                targetLang = it.sourceLang,
                sourceText = it.translatedText,
                translatedText = it.sourceText
            )
        }
    }

    fun setStyle(style: TranslationStyle) {
        _uiState.update { it.copy(style = style) }
    }

    fun translate() {
        val state = _uiState.value
        if (state.sourceText.isBlank()) return

        viewModelScope.launch {
            if (!translationEngine.isModelLoaded) {
                val path = modelManager.getModelPath()
                if (path != null) {
                    translationEngine.loadModel(path)
                }
            }
            _uiState.update { it.copy(isTranslating = true, error = null) }

            val result = translationEngine.translate(
                text = state.sourceText,
                sourceLang = state.sourceLang,
                targetLang = state.targetLang,
                style = state.style
            )

            result.fold(
                onSuccess = { translated ->
                    _uiState.update {
                        it.copy(
                            translatedText = translated,
                            isTranslating = false,
                            isModelReady = true
                        )
                    }
                    historyRepository.saveTranslation(
                        sourceText = state.sourceText,
                        translatedText = translated,
                        sourceLang = state.sourceLang,
                        targetLang = state.targetLang
                    )
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isTranslating = false,
                            error = e.message ?: "翻訳に失敗しました"
                        )
                    }
                }
            )
        }
    }

    fun clear() {
        _uiState.update {
            it.copy(sourceText = "", translatedText = "", error = null)
        }
    }
}
