package com.hassaku.plamotranslate.data.repository

import com.hassaku.plamotranslate.data.local.HistoryDao
import com.hassaku.plamotranslate.data.model.TranslationRecord
import com.hassaku.plamotranslate.data.model.WordRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao
) {
    fun getRecentTranslations(limit: Int = 50): Flow<List<TranslationRecord>> =
        historyDao.getRecentTranslations(limit)

    fun getFavoriteTranslations(): Flow<List<TranslationRecord>> =
        historyDao.getFavoriteTranslations()

    suspend fun saveTranslation(
        sourceText: String,
        translatedText: String,
        sourceLang: String,
        targetLang: String
    ): Long {
        return historyDao.insertTranslation(
            TranslationRecord(
                sourceText = sourceText,
                translatedText = translatedText,
                sourceLang = sourceLang,
                targetLang = targetLang
            )
        )
    }

    suspend fun toggleTranslationFavorite(record: TranslationRecord) {
        historyDao.updateTranslation(record.copy(isFavorite = !record.isFavorite))
    }

    suspend fun deleteTranslation(id: Long) = historyDao.deleteTranslation(id)

    fun getRecentWords(limit: Int = 50): Flow<List<WordRecord>> =
        historyDao.getRecentWords(limit)

    suspend fun saveWord(word: String, mainMeaningJa: String, cefr: String?): Long {
        return historyDao.insertWord(
            WordRecord(word = word, mainMeaningJa = mainMeaningJa, cefr = cefr)
        )
    }

    suspend fun toggleWordFavorite(record: WordRecord) {
        historyDao.updateWord(record.copy(isFavorite = !record.isFavorite))
    }
}
