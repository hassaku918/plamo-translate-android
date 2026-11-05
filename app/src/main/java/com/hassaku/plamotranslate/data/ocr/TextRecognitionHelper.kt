package com.hassaku.plamotranslate.data.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.hassaku.plamotranslate.ui.camera.DetectedTextBlock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextRecognitionHelper @Inject constructor() {

    private val latinRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private val japaneseRecognizer by lazy {
        TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
    }

    /**
     * Runs both Latin + Japanese recognizers and merges blocks.
     */
    suspend fun recognize(bitmap: Bitmap): List<DetectedTextBlock> {
        val image = InputImage.fromBitmap(bitmap, 0)
        val results = mutableListOf<DetectedTextBlock>()
        var id = 0

        try {
            val ja = japaneseRecognizer.process(image).await()
            for (block in ja.textBlocks) {
                val box = block.boundingBox
                val bounds = if (box != null && bitmap.width > 0 && bitmap.height > 0) {
                    floatArrayOf(
                        box.left.toFloat() / bitmap.width,
                        box.top.toFloat() / bitmap.height,
                        box.right.toFloat() / bitmap.width,
                        box.bottom.toFloat() / bitmap.height
                    )
                } else floatArrayOf(0f, 0f, 0f, 0f)

                results.add(
                    DetectedTextBlock(
                        id = id++,
                        text = block.text,
                        bounds = bounds
                    )
                )
            }
        } catch (_: Exception) { /* fall through */ }

        // If Japanese found little, also try Latin
        if (results.isEmpty()) {
            try {
                val en = latinRecognizer.process(image).await()
                for (block in en.textBlocks) {
                    val box = block.boundingBox
                    val bounds = if (box != null && bitmap.width > 0 && bitmap.height > 0) {
                        floatArrayOf(
                            box.left.toFloat() / bitmap.width,
                            box.top.toFloat() / bitmap.height,
                            box.right.toFloat() / bitmap.width,
                            box.bottom.toFloat() / bitmap.height
                        )
                    } else floatArrayOf(0f, 0f, 0f, 0f)

                    results.add(
                        DetectedTextBlock(
                            id = id++,
                            text = block.text,
                            bounds = bounds
                        )
                    )
                }
            } catch (_: Exception) { }
        }

        return results
    }

    fun close() {
        latinRecognizer.close()
        japaneseRecognizer.close()
    }
}
