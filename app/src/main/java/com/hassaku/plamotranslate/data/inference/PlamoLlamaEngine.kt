package com.hassaku.plamotranslate.data.inference

import android.content.Context
import android.net.Uri
import com.hassaku.plamotranslate.ui.translate.TranslationStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.nehuatl.llamacpp.LlamaHelper
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Real on-device PLaMo translation using kotlinllamacpp (llama.cpp).
 */
@Singleton
class PlamoLlamaEngine @Inject constructor(
    @ApplicationContext private val context: Context
) : TranslationEngine {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val eventFlow = MutableSharedFlow<LlamaHelper.LLMEvent>(
        extraBufferCapacity = 128,
        onBufferOverflow = kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
    )

    private var helper: LlamaHelper? = null
    private val loaded = AtomicBoolean(false)
    private var libraryAvailable = true

    init {
        try {
            helper = LlamaHelper(context.contentResolver, scope, eventFlow)
        } catch (t: Throwable) {
            libraryAvailable = false
        }
    }

    override val isModelLoaded: Boolean
        get() = loaded.get()

    override suspend fun loadModel(modelPath: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (!libraryAvailable || helper == null) {
                return@withContext Result.failure(
                    Exception("llama.cpp ライブラリが利用できません。依存関係を確認してください。")
                )
            }
            val file = File(modelPath)
            if (!file.exists() || file.length() < 1_000_000) {
                return@withContext Result.failure(
                    Exception("モデルファイルが見つかりません: $modelPath")
                )
            }
            try {
                val uri = Uri.fromFile(file).toString()
                var done = false
                var error: String? = null
                helper!!.load(path = uri, contextLength = 2048) { _ ->
                    done = true
                    loaded.set(true)
                }
                // Wait briefly for load callback
                var waited = 0
                while (!done && error == null && waited < 120_000) {
                    kotlinx.coroutines.delay(200)
                    waited += 200
                    if (loaded.get()) break
                }
                if (loaded.get()) Result.success(Unit)
                else Result.failure(Exception(error ?: "モデルのロードがタイムアウトしました"))
            } catch (e: Exception) {
                loaded.set(false)
                Result.failure(e)
            }
        }

    override suspend fun unloadModel() {
        loaded.set(false)
        // LlamaHelper may not expose unload; mark unloaded for our state
    }

    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String,
        style: TranslationStyle
    ): Result<String> = withContext(Dispatchers.IO) {
        if (!loaded.get() || helper == null) {
            return@withContext Result.failure(
                Exception("モデル未ロードです。設定から PLaMo GGUF をダウンロード＆ロードしてください。")
            )
        }
        try {
            val prompt = PlamoPromptBuilder.build(text, sourceLang, targetLang, style)
            val sb = StringBuilder()
            val collectJob = scope.launch {
                eventFlow.collect { event ->
                    when (event) {
                        is LlamaHelper.LLMEvent.Ongoing -> sb.append(event.word)
                        is LlamaHelper.LLMEvent.Done -> { /* finished */ }
                        is LlamaHelper.LLMEvent.Error -> { /* handled below */ }
                        else -> {}
                    }
                }
            }
            helper!!.predict(prompt)
            // Wait for generation (max 90s)
            withTimeout(90_000) {
                var idle = 0
                var lastLen = 0
                while (idle < 30) {
                    kotlinx.coroutines.delay(300)
                    if (sb.length == lastLen) idle++ else {
                        idle = 0
                        lastLen = sb.length
                    }
                    if (idle >= 8 && sb.isNotEmpty()) break
                }
            }
            collectJob.cancel()
            val out = sb.toString().trim()
            if (out.isEmpty()) {
                Result.failure(Exception("生成結果が空でした。プロンプトやモデルを確認してください。"))
            } else {
                Result.success(out)
            }
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
        val result = translate(text, sourceLang, targetLang, style)
        result.getOrNull()?.let { emit(it) }
    }

    fun isLibraryAvailable(): Boolean = libraryAvailable
}
