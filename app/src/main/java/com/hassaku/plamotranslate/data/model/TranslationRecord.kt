package com.hassaku.plamotranslate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "word_history")
data class WordRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val mainMeaningJa: String,
    val cefr: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
