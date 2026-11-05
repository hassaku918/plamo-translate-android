package com.hassaku.plamotranslate.data.model

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

data class ModelInfo(
    val name: String = "PLaMo 3 Translate 0.8B",
    val quant: String = "Q4_K_M",
    val expectedFileName: String = "plamo-3-translate-0.8b-Q4_K_M.gguf",
    /** Hugging Face resolve URL (may need adjustment to actual filename on HF) */
    val downloadUrl: String =
        "https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF/resolve/main/plamo-3-translate-0.8b-Q4_K_M.gguf",
    val huggingFacePage: String = "https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF",
    val isDownloaded: Boolean = false,
    val localPath: String? = null,
    val sizeMb: Int = 550
)

data class ModelDownloadState(
    val info: ModelInfo = ModelInfo(),
    val isDownloading: Boolean = false,
    val progress: Float = 0f,
    val error: String? = null,
    val isLoaded: Boolean = false
)

@Singleton
class ModelManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _state = MutableStateFlow(ModelDownloadState())
    val state: StateFlow<ModelDownloadState> = _state.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

    private val modelsDir: File
        get() = File(context.filesDir, "models").also { it.mkdirs() }

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        val expected = _state.value.info.expectedFileName
        // Also accept any *.gguf in models dir
        val preferred = File(modelsDir, expected)
        val anyGguf = modelsDir.listFiles()?.firstOrNull { it.extension == "gguf" && it.length() > 1_000_000 }
        val file = when {
            preferred.exists() && preferred.length() > 1_000_000 -> preferred
            anyGguf != null -> anyGguf
            else -> null
        }
        _state.value = _state.value.copy(
            info = _state.value.info.copy(
                isDownloaded = file != null,
                localPath = file?.absolutePath,
                expectedFileName = file?.name ?: expected
            ),
            error = null
        )
    }

    fun getModelPath(): String? = _state.value.info.localPath

    fun markLoaded(loaded: Boolean) {
        _state.value = _state.value.copy(isLoaded = loaded)
    }

    suspend fun downloadModel(): Result<Unit> = withContext(Dispatchers.IO) {
        _state.value = _state.value.copy(isDownloading = true, progress = 0f, error = null)
        val dest = File(modelsDir, _state.value.info.expectedFileName)
        val tmp = File(modelsDir, dest.name + ".part")
        try {
            val request = Request.Builder()
                .url(_state.value.info.downloadUrl)
                .header("User-Agent", "PLaMoTranslateAndroid/0.2")
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception(
                        "ダウンロード失敗 HTTP ${response.code}。" +
                            " Hugging Face のファイル名が違う可能性があります。" +
                            " ページから手動配置もできます: ${_state.value.info.huggingFacePage}"
                    )
                }
                val body = response.body ?: throw Exception("空のレスポンス")
                val total = body.contentLength().coerceAtLeast(1L)
                body.byteStream().use { input ->
                    tmp.outputStream().use { output ->
                        val buf = ByteArray(256 * 1024)
                        var read: Int
                        var sum = 0L
                        while (input.read(buf).also { read = it } != -1) {
                            output.write(buf, 0, read)
                            sum += read
                            _state.value = _state.value.copy(progress = (sum.toFloat() / total).coerceIn(0f, 1f))
                        }
                    }
                }
            }
            if (dest.exists()) dest.delete()
            if (!tmp.renameTo(dest)) {
                tmp.copyTo(dest, overwrite = true)
                tmp.delete()
            }
            refreshStatus()
            _state.value = _state.value.copy(isDownloading = false, progress = 1f)
            Result.success(Unit)
        } catch (e: Exception) {
            tmp.delete()
            _state.value = _state.value.copy(
                isDownloading = false,
                error = e.message ?: "ダウンロードエラー"
            )
            Result.failure(e)
        }
    }
}
