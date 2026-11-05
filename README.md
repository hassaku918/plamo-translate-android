# PLaMo Translate Android

**完全オフライン**で動く高品質翻訳アプリ。Preferred Networksの **PLaMo 3 Translate 0.8B** をオンデバイスで動作させ、Google Lens風のカメラテキスト選択にも対応。

> PFN公式デモのように機内モードでも高速・自然な翻訳が可能です。

## 現在の進捗（2026-11-05）

### 実装済み
- [x] プロジェクト骨格（Gradle + Compose + Hilt）
- [x] **HorizontalPager によるスワイプ切り替え**（Grok風）
- [x] ボトムナビゲーション連動
- [x] 翻訳画面（UI骨格）
- [x] **単語モード画面（ChatGPT風リッチ辞書UIデモ）**
  - 発音記号 / CEFRバッジ / 品詞
  - 主要意味 + 詳細説明
  - 典型的な用法・例文（日英）
  - 類義語比較・語法ポイント
- [x] カメラ画面（プレースホルダー）

### これから
- [ ] PLaMo (llama.cpp) 推論連携
- [ ] オフライン辞書データ（多義・CEFR）
- [ ] CameraX + ML Kit OCR 実装
- [ ] 履歴・お気に入り
- [ ] モデルダウンロード管理
- [ ] GitHub Actions でAPK自動ビルド

## 特徴

### 翻訳エンジン
- PLaMo 3 Translate 0.8B (GGUF) 完全ローカル推論
- 推奨量子化: **Q4_K_M**
- 日本語 常体 / 敬体 指定
- ストリーミング生成・長文分割

### 単語モード（独立 + スワイプ対応）
- 左右スワイプで「翻訳 ⇄ 単語 ⇄ カメラ」を移動
- ChatGPT共有スタイルのリッチな辞書表示
- CEFRレベルバッジ（A1〜C2）
- 多義・例文・類義語比較を完全オフラインで

### カメラOCR（Lens風）
- CameraX + ML Kit Text Recognition
- リアルタイム検出 → タップで選択 → 翻訳/単語モードへ

## ビルド方法

### 必要環境
- Android Studio Ladybug 以降
- JDK 17
- NDK（llama.cpp連携時）

### 手順
1. リポジトリをclone
   ```bash
   git clone https://github.com/hassaku918/plamo-translate-android.git
   ```
2. Android Studioで開く
3. Gradle Sync
4. 実機またはエミュレータでRun

> 現時点ではアイコンリソースが未配置のため、初回は `mipmap` エラーが出る可能性があります。  
> Android Studioの **Image Asset Studio** で適当なアイコンを生成してください。

## ナビゲーション

| スワイプ | 画面 | 内容 |
|---------|------|------|
| 左 | 翻訳 | 文章翻訳 |
| 中央 | 単語 | 多義・CEFR・リッチ辞書UI |
| 右 | カメラ | Lens風OCR |

ボトムナビをタップしても同じページに移動します。

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| UI | Jetpack Compose + Material 3 |
| スワイプ | Foundation `HorizontalPager` |
| DI | Hilt |
| OCR | CameraX + ML Kit |
| 辞書 | Room + バンドルSQLite（予定） |
| 推論 | llama.cpp (予定) |

## ライセンス

- アプリコード: MIT（予定）
- PLaMo 3 Translate: [PLaMo Community License](https://plamo.preferredai.jp/info/plamo-community-license-ja)

---

Made for offline-first, privacy-respecting translation & vocabulary learning.
Powered by PLaMo 3 Translate from Preferred Networks.
