package com.hassaku.plamotranslate.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassaku.plamotranslate.data.dictionary.DemoDictionary
import com.hassaku.plamotranslate.data.model.WordEntry
import com.hassaku.plamotranslate.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WordUiState(
    val query: String = "",
    val result: WordEntry? = null,
    val isLoading: Boolean = false,
    val notFound: Boolean = false
)

@HiltViewModel
class WordViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordUiState())
    val uiState: StateFlow<WordUiState> = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query, notFound = false) }
        if (query.isBlank()) {
            _uiState.update { it.copy(result = null) }
            return
        }
        search(query)
    }

    fun search(query: String = _uiState.value.query) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, notFound = false) }

            val entry = DemoDictionary.lookup(query)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    result = entry,
                    notFound = entry == null && query.isNotBlank()
                )
            }

            // Save to word history when found
            entry?.let {
                historyRepository.saveWord(
                    word = it.word,
                    mainMeaningJa = it.mainMeaningJa,
                    cefr = it.cefr
                )
            }
        }
    }

    fun clear() {
        _uiState.update { WordUiState() }
    }
}
