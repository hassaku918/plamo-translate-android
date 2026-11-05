package com.hassaku.plamotranslate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hassaku.plamotranslate.data.model.TranslationRecord
import com.hassaku.plamotranslate.data.model.WordRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    // Translation history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(record: TranslationRecord): Long

    @Query("SELECT * FROM translation_history ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentTranslations(limit: Int = 50): Flow<List<TranslationRecord>>

    @Query("SELECT * FROM translation_history WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteTranslations(): Flow<List<TranslationRecord>>

    @Update
    suspend fun updateTranslation(record: TranslationRecord)

    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteTranslation(id: Long)

    @Query("DELETE FROM translation_history")
    suspend fun clearTranslationHistory()

    // Word history
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(record: WordRecord): Long

    @Query("SELECT * FROM word_history ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentWords(limit: Int = 50): Flow<List<WordRecord>>

    @Query("SELECT * FROM word_history WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteWords(): Flow<List<WordRecord>>

    @Update
    suspend fun updateWord(record: WordRecord)

    @Query("DELETE FROM word_history WHERE id = :id")
    suspend fun deleteWord(id: Long)

    @Query("DELETE FROM word_history")
    suspend fun clearWordHistory()
}
