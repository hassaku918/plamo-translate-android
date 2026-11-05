package com.hassaku.plamotranslate.data.inference

import com.hassaku.plamotranslate.ui.translate.TranslationStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real engine backed by llama.cpp via JNI.
 * Currently falls back to clear error until native lib is built with llama.cpp.
 */
@Singleton
class LlamaCppEngine @Inject constructor() : TranslationEngine {

    companion object {
        init {
            try {
                System.loadLibrary("plamo_jni")
            } catch (e: UnsatisfiedLinkError) {
                // Native lib not built yet — expected during UI-only development
            }
        }
    }

    private external fun nativeLoadModel(modelPath: String): Boolean
    private external fun nativeUnloadModel()
    private external fun nativeGenerate(prompt: String, maxTokens: Int): String
    private external fun nativeIsLoaded(): Boolean

    override val isModelLoaded: Boolean
        get() = try {
            nativeIsLoaded()
        } catch (_: UnsatisfiedLinkError) {
            false
        }

    override suspend fun loadModel(modelPath: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val ok = nativeLoadModel(modelPath)
                if (ok) Result.success(Unit)
                else Result.failure(Exception("モデルのロードに失敗しました（JNIスタブ）"))
            } catch (e: UnsatisfiedLinkError) {
                Result.failure(Exception("ネイティブライブラリが未ビルドです。llama.cpp を統合してください。"))
            }
        }

    override suspend fun unloadModel() = withContext(Dispatchers.IO) {
        try {
            nativeUnloadModel()
        } catch (_: UnsatisfiedLinkError) { }
    }

    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (!nativeIsLoaded()) {
                return@withContext Result.failure(
                    Exception("モデルがロードされていません。設定からGGUFを配置・ロードしてください。")
                )
            }
            val prompt = PlamoPromptBuilder.build(text, sourceLang, targetLang, style)
            val out = nativeGenerate(prompt, 512)
            if (out.isBlank()) {
                Result.failure(Exception("生成結果が空です（JNIスタブ状態）"))
            } else {
                Result.success(out.trim())
            }
        } catch (e: UnsatisfiedLinkError) {
            Result.failure(Exception("ネイティブライブラリ未接続"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun translateStream(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): Flow<String> = flow {
        // Full result first; streaming can be added when native callback is ready
        val result = translate(text, sourceLang, targetLang, style)
        result.getOrNull()?.let { emit(it) }
    }
}
