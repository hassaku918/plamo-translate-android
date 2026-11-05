package com.hassaku.plamotranslate.data.inference

import com.hassaku.plamotranslate.ui.translate.TranslationStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Placeholder engine used until llama.cpp is integrated.
 */
@Singleton
class MockTranslationEngine @Inject constructor() : TranslationEngine {

    @Volatile
    private var loaded = false

    override val isModelLoaded: Boolean
        get() = loaded

    override suspend fun loadModel(modelPath: String): Result<Unit> {
        delay(500) // simulate load
        loaded = true
        return Result.success(Unit)
    }

    override suspend fun unloadModel() {
        loaded = false
    }

    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): Result<String> {
        delay(300)
        val styleNote = when (style) {
            TranslationStyle.POLITE -> "（敬体）"
            TranslationStyle.CASUAL -> "（常体）"
            TranslationStyle.NORMAL -> ""
        }
        return Result.success(
            buildString {
                appendLine("【デモ翻訳 $styleNote】")
                appendLine("$sourceLang → $targetLang")
                appendLine()
                appendLine(text)
                appendLine()
                append("※ PLaMo GGUF 連携後に置き換わります")
            }
        )
    }

    override fun translateStream(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): Flow<String> = flow {
        val full = translate(text, sourceLang, targetLang, style).getOrThrow()
        val words = full.split(" ")
        val sb = StringBuilder()
        for (w in words) {
            sb.append(w).append(" ")
            emit(sb.toString())
            delay(40)
        }
    }
}
