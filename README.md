# PLaMo Translate Android

**完全オフライン**で動く高品質翻訳＋単語学習アプリ。  
Preferred Networks の **PLaMo 3 Translate 0.8B** をオンデバイスで動かすことを目指した実装です。

## 現在の完成度（2026-11-05）

### 実装済み ✅

| 機能 | 状態 |
|------|------|
| HorizontalPager スワイプ切り替え（Grok風） | ✅ |
| ボトムナビ連動 | ✅ |
| 翻訳画面 + 常体/敬体スタイル | ✅ |
| 単語モード（ChatGPT風リッチ辞書UI） | ✅ |
| デモ辞書（connoisseur / gut / resilient） | ✅ |
| CEFRバッジ・多義・例文・類義語比較 | ✅ |
| 履歴（翻訳・単語）+ お気に入り | ✅ |
| 設定画面（モデル・推論パラメータ） | ✅ |
| モデル管理スケルトン | ✅ |
| 推論エンジン抽象化（TranslationEngine） | ✅ |
| PLaMo公式プロンプトビルダー | ✅ |
| Camera権限 + OCR画面骨格 | ✅ |
| Room + Hilt + Cleanっぽい構成 | ✅ |
| GitHub Actions APKビルドワークフロー | ✅ |

### これから（ネイティブ連携）

| 機能 | 状態 |
|------|------|
| llama.cpp JNI / GGUF読み込み | 🔲 |
| 本格オフライン辞書（SQLiteバンドル） | 🔲 |
| CameraX Preview + ML Kit リアルタイムOCR | 🔲 |
| バウンディングボックス選択UI | 🔲 |
| 実モデルダウンロード | 🔲 |

## 使い方

```bash
git clone https://github.com/hassaku918/plamo-translate-android.git
```

1. Android Studio で開く
2. Gradle Sync
3. 実機 or エミュレータで Run

### 単語モードで試せる単語
- `connoisseur`
- `gut`
- `resilient`

### 操作
- **左右スワイプ** … 翻訳 ⇄ 単語 ⇄ カメラ
- **右上の履歴アイコン** … 翻訳・単語履歴
- **右上の歯車** … 設定・モデル管理

## アーキテクチャ概要

```
ui/
  navigation/   HorizontalPager + Overlay (History/Settings)
  screens/      Translate / Word / Camera / History / Settings
  translate/    TranslateViewModel
  word/         WordViewModel
  camera/       CameraViewModel
  history/      HistoryViewModel
data/
  inference/    TranslationEngine + Mock + PlamoPromptBuilder
  dictionary/   DemoDictionary
  local/        Room DB + DAO
  repository/   HistoryRepository
  model/        ModelManager, WordEntry, etc.
di/             Hilt modules
```

## PLaMo モデルについて

推奨: [pfnet/plamo-3-translate-0.8b-GGUF](https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF) の **Q4_K_M**

プロンプト形式:
```
<|plamo:op|>dataset
translation
<|plamo:op|>input lang=English
[原文]
<|plamo:op|>output lang=Japanese
```

## ビルド（GitHub Actions）

`main` への push で Debug APK を自動ビルドし、Artifacts にアップロードします。  
（Gradle Wrapper の jar が無い場合はローカルで `gradle wrapper` を実行してください）

## ライセンス

- アプリコード: MIT（予定）
- PLaMo 3 Translate モデル: [PLaMo Community License](https://plamo.preferredai.jp/info/plamo-community-license-ja)

---

Made for offline-first, privacy-respecting translation & vocabulary learning.  
Powered by PLaMo 3 Translate from Preferred Networks.
