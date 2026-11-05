package com.hassaku.plamotranslate.data.model

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class ModelInfo(
    val name: String = "PLaMo 3 Translate 0.8B",
    val quant: String = "Q4_K_M",
    val expectedFileName: String = "plamo-3-translate-0.8b-Q4_K_M.gguf",
    val huggingFaceUrl: String = "https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF",
    val isDownloaded: Boolean = false,
    val localPath: String? = null,
    val sizeMb: Int = 500
)

data class ModelDownloadState(
    val info: ModelInfo = ModelInfo(),
    val isDownloading: Boolean = false,
    val progress: Float = 0f,
    val error: String? = null
)

@Singleton
class ModelManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _state = MutableStateFlow(ModelDownloadState())
    val state: StateFlow<ModelDownloadState> = _state.asStateFlow()

    private val modelsDir: File
        get() = File(context.filesDir, "models").also { it.mkdirs() }

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        val expected = _state.value.info.expectedFileName
        val file = File(modelsDir, expected)
        val exists = file.exists() && file.length() > 1_000_000
        _state.value = _state.value.copy(
            info = _state.value.info.copy(
                isDownloaded = exists,
                localPath = if (exists) file.absolutePath else null
            )
        )
    }

    fun getModelPath(): String? = _state.value.info.localPath

    /**
     * Placeholder for real download.
     * In production: use DownloadManager or OkHttp with progress.
     */
    suspend fun downloadModel(): Result<Unit> = withContext(Dispatchers.IO) {
        _state.value = _state.value.copy(isDownloading = true, progress = 0f, error = null)
        try {
            // Simulate download progress
            for (i in 1..10) {
                kotlinx.coroutines.delay(200)
                _state.value = _state.value.copy(progress = i / 10f)
            }
            // Real implementation would write the GGUF file here.
            // For now we just mark as "not actually downloaded".
            _state.value = _state.value.copy(
                isDownloading = false,
                progress = 0f,
                error = "実際のダウンロードは未実装です。Hugging Faceから手動でGGUFを取得し、app/src/main/assets/models/ または内部ストレージの models/ に配置してください。"
            )
            Result.failure(Exception("Manual download required"))
        } catch (e: Exception) {
            _state.value = _state.value.copy(isDownloading = false, error = e.message)
            Result.failure(e)
        }
    }
}
