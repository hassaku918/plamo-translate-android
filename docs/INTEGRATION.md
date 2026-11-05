# 統合ガイド

## llama.cpp を繋ぐ手順

1. ソース取得
   ```bash
   cd app/src/main/cpp
   git clone --depth 1 https://github.com/ggerganov/llama.cpp
   ```

2. `CMakeLists.txt` を編集
   - `add_subdirectory(llama.cpp ...)` を有効化
   - `target_link_libraries(plamo_jni ... llama)` を有効化

3. `plamo_jni.cpp` を実装
   - `llama_model_load_from_file`
   - `llama_init_from_model` / context
   - `llama_decode` + sampling
   - プロンプトは `PlamoPromptBuilder` が生成した文字列を渡す

4. モデル配置
   - Hugging Face から Q4_K_M をダウンロード
   - 端末の `context.filesDir/models/` へコピー
   - 設定画面の「モデルをダウンロード」を本実装に差し替え可能

5. DI
   - `InferenceModule` で `LlamaCppEngine` を bind
   - ロード成功後に `TranslateViewModel` から利用

## ML Kit + CameraX

1. `CameraScreen` に `AndroidView({ PreviewView(it) })`
2. `ProcessCameraProvider` で Preview + ImageAnalysis を bind
3. Analysis の毎フレーム（間引き推奨）で Bitmap 化
4. `TextRecognitionHelper.recognize(bitmap)`
5. 結果を `CameraViewModel.onTextDetected`
6. オーバーレイで bounds を描画、タップで `selectBlock`
7. 「翻訳する」で翻訳タブへテキストを渡す（SharedViewModel or コールバック）

## 辞書の本実装

- assets に `dictionary.db` を同梱
- Room の `createFromAsset` で開く
- スキーマは `WordEntry` に合わせる
- CEFR は English Vocabulary Profile 等のオープンデータを利用（ライセンス確認）
