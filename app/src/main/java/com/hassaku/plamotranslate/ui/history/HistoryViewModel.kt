package com.hassaku.plamotranslate.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassaku.plamotranslate.data.model.TranslationRecord
import com.hassaku.plamotranslate.data.model.WordRecord
import com.hassaku.plamotranslate.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val translations: List<TranslationRecord> = emptyList(),
    val words: List<WordRecord> = emptyList(),
    val selectedTab: HistoryTab = HistoryTab.TRANSLATIONS
)

enum class HistoryTab { TRANSLATIONS, WORDS }

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = combine(
        historyRepository.getRecentTranslations(),
        historyRepository.getRecentWords()
    ) { translations, words ->
        HistoryUiState(translations = translations, words = words)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState()
    )

    fun toggleTranslationFavorite(record: TranslationRecord) {
        viewModelScope.launch {
            historyRepository.toggleTranslationFavorite(record)
        }
    }

    fun deleteTranslation(id: Long) {
        viewModelScope.launch {
            historyRepository.deleteTranslation(id)
        }
    }

    fun toggleWordFavorite(record: WordRecord) {
        viewModelScope.launch {
            historyRepository.toggleWordFavorite(record)
        }
    }
}
