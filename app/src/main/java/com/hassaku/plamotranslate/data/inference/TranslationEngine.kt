package com.hassaku.plamotranslate.data.inference

import com.hassaku.plamotranslate.ui.translate.TranslationStyle
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for on-device translation.
 * Real implementation will use llama.cpp + PLaMo GGUF.
 */
interface TranslationEngine {
    val isModelLoaded: Boolean
    suspend fun loadModel(modelPath: String): Result<Unit>
    suspend fun unloadModel()
    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle = TranslationStyle.NORMAL
    ): Result<String>

    /** Streaming version for token-by-token UI updates */
    fun translateStream(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle = TranslationStyle.NORMAL
    ): Flow<String>
}

/**
 * Builds the official PLaMo translation prompt.
 */
object PlamoPromptBuilder {
    fun build(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): String {
        val styleHint = when (style) {
            TranslationStyle.POLITE -> "\n出力は敬体（です・ます調）で書いてください。"
            TranslationStyle.CASUAL -> "\n出力は常体（だ・である調）で書いてください。"
            TranslationStyle.NORMAL -> ""
        }
        return buildString {
            appendLine("<|plamo:op|>dataset")
            appendLine("translation")
            appendLine("<|plamo:op|>input lang=$sourceLang")
            appendLine(text.trim())
            append("<|plamo:op|>output lang=$targetLang")
            if (styleHint.isNotEmpty()) append(styleHint)
        }
    }
}
