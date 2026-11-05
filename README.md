# PLaMo Translate Android

**完全オフライン**の翻訳＋単語学習アプリ。  
Preferred Networks **PLaMo 3 Translate 0.8B** のオンデバイス利用を目指した実装です。

## リポジトリ
https://github.com/hassaku918/plamo-translate-android

## 完成している機能

| 機能 | 状態 |
|------|------|
| Grok風スワイプ（翻訳 ⇄ 単語 ⇄ カメラ） | ✅ |
| ボトムナビ + TopBar（履歴・設定） | ✅ |
| 翻訳（常体/敬体）+ 履歴保存 | ✅ |
| 単語モード（CEFR・多義・例文・類義語） | ✅ |
| デモ辞書 8語 | ✅ |
| 履歴・お気に入り（Room） | ✅ |
| 設定・モデル管理UI | ✅ |
| TranslationEngine 抽象化 | ✅ |
| PLaMo公式プロンプトビルダー | ✅ |
| llama.cpp JNI 骨格（CMake + stub） | ✅ |
| ML Kit テキスト認識ヘルパー | ✅ |
| Camera権限・選択UI | ✅ |
| GitHub Actions APK workflow | ✅ |

## 単語モードで試せる語

`connoisseur` / `gut` / `resilient` / `ephemeral` / `pragmatic` / `ambiguous` / `meticulous` / `ubiquitous`

## ビルド手順

```bash
git clone https://github.com/hassaku918/plamo-translate-android.git
cd plamo-translate-android
```

1. Android Studio (Ladybug+) で開く
2. NDK / CMake が入っていることを確認
3. Gradle Sync → Run

初回は Gradle Wrapper が無い場合:
```bash
gradle wrapper --gradle-version 8.9
./gradlew assembleDebug
```

## 残作業（実機で本格動作させるために）

### 1. llama.cpp 統合
```bash
cd app/src/main/cpp
git clone https://github.com/ggerganov/llama.cpp
# CMakeLists.txt のコメントを外して llama をリンク
# plamo_jni.cpp を実API呼び出しに書き換え
```

モデル: [pfnet/plamo-3-translate-0.8b-GGUF](https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF) の Q4_K_M  
配置先: アプリ内部 `files/models/` または assets

### 2. CameraX Preview
`CameraScreen` に `AndroidView(PreviewView)` を埋め込み、  
`ImageAnalysis` → `TextRecognitionHelper.recognize()` でブロック更新。

### 3. 本格辞書
`DemoDictionary` を SQLite / Room のバンドルDBに置き換え。  
CEFR付き英単語リストを assets に同梱。

## アーキテクチャ

```
ui/          Compose画面 + ViewModel
data/
  inference/ TranslationEngine, Mock, LlamaCpp, PromptBuilder
  dictionary/ DemoDictionary
  ocr/       TextRecognitionHelper (ML Kit)
  local/     Room
  repository/
cpp/         JNI + CMake（llama.cpp 接続点）
```

## ライセンス

- アプリコード: MIT（予定）
- PLaMo: [PLaMo Community License](https://plamo.preferredai.jp/info/plamo-community-license-ja)

---
Offline-first translation & vocabulary learning.  
Powered by PLaMo 3 Translate (Preferred Networks).
