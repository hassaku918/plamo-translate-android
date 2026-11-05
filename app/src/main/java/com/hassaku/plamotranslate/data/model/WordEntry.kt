package com.hassaku.plamotranslate.data.model

/**
 * Offline dictionary entry for Word Mode.
 * Designed to support the rich ChatGPT-style display.
 */
data class WordEntry(
    val word: String,
    val phonetic: String? = null,
    val cefr: String? = null,          // e.g. "B2", "C1–C2"
    val partOfSpeech: String? = null,  // 名詞, 動詞, etc.
    val mainMeaningJa: String,
    val explanationJa: String? = null,
    val usages: List<UsageExample> = emptyList(),
    val exampleSentences: List<ExampleSentence> = emptyList(),
    val synonyms: List<SynonymComparison> = emptyList(),
    val usageNotes: String? = null,
    val jlpt: String? = null           // optional JLPT level for Japanese words
)

data class UsageExample(
    val expression: String,
    val meaningJa: String
)

data class ExampleSentence(
    val english: String,
    val japanese: String
)

data class SynonymComparison(
    val word: String,
    val nuanceJa: String,
    val isHighlight: Boolean = false
)
