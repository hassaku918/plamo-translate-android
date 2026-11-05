# PLaMo Translate Android

**完全オフライン**で動く高品質翻訳アプリ。Preferred Networksの **PLaMo 3 Translate 0.8B** をオンデバイスで動作させ、Google Lens風のカメラテキスト選択にも対応。

> PFN公式デモのように機内モードでも高速・自然な翻訳が可能です。

## 特徴（機能盛り盛り）

### 翻訳エンジン
- PLaMo 3 Translate 0.8B (GGUF) 完全ローカル推論
- 推奨量子化: **Q4_K_M**（品質とサイズのバランス最良）
- 53言語対応（日英を中心に多言語双方向）
- 日本語 **常体 / 敬体** 指定
- 周辺コンテキスト・用語集（glossary）サポート
- ストリーミング生成（トークン単位でリアルタイム表示）
- 長文自動分割翻訳
- プロンプトフォーマット完全準拠:
  ```
  <|plamo:op|>dataset
  translation
  <|plamo:op|>input lang=English
  [原文]
  <|plamo:op|>output lang=Japanese
  ```

### 単語モード（オフライン辞書連携）★NEW
- **単一単語検出**: 入力が単語（または短いフレーズ）の場合に自動切替
- **多義（複数の意味）表示**: オフライン辞書から複数の語義・品詞・例文を表示
- **CEFRレベル表示**: その単語のCEFRレベル（A1〜C2）をバッジで表示
- 完全オフライン（バンドルしたSQLite辞書を使用）
- 対応予定: 英語中心 + 日本語（JLPTとの対応も検討）
- 高頻度語を優先してコンパクトに収録（アプリサイズを抑えつつ実用性を確保）

### カメラOCR（Lens風）
- CameraX + ML Kit Text Recognition v2
- リアルタイムテキスト検出オーバーレイ
- 検出ブロック/行をタップして選択
- 選択テキストを即座に翻訳画面へ送る
- ギャラリー画像からのOCRも対応
- バウンディングボックスハイライト

### UI / その他機能
- Jetpack Compose + Material 3
- ダーク / ライトテーマ自動切替
- 翻訳履歴（Room DB）
- お気に入り登録
- コピー / 共有 / TTS読み上げ
- モデルダウンロード管理（進捗・キャンセル対応）
- 詳細設定（スレッド数・コンテキスト長・温度・Top-Pなど）
- オフライン完全対応（モデル取得後はネット不要）

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| UI | Jetpack Compose + Material 3 |
| アーキテクチャ | MVVM + Clean Architecture |
| 非同期 | Kotlin Coroutines + Flow |
| 推論 | llama.cpp (GGUF) via JNI / kotlinllamacpp |
| OCR | ML Kit Text Recognition + CameraX |
| 辞書 | Room + バンドルSQLite（多義・CEFR） |
| 永続化 | Room + DataStore Preferences |
| DI | Hilt (推奨) |

## 推奨モデル

Hugging Faceから以下をダウンロードしてください：

- **メイン推奨**: [pfnet/plamo-3-translate-0.8b-GGUF](https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF)
  - Q4_K_M または Q4_0 がモバイル最適

0.8Bモデルは現代のスマホ（6GB+ RAM推奨）で快適に動作します。

## ビルド方法

### 必要環境
- Android Studio Ladybug 以降推奨
- NDK r26+
- CMake 3.22+
- JDK 17
- 実機推奨（エミュレータでも可だが遅い）

### 手順
1. このリポジトリをclone
2. Android Studioで開く
3. `local.properties` にSDKパスを設定
4. モデルを `app/src/main/assets/models/` に置くか、アプリ内ダウンロード機能を使用
5. Run

詳細なセットアップは順次追加していきます。

## ライセンス注意

- アプリコード: MIT（予定）
- **PLaMo 3 Translateモデル**: [PLaMo Community License](https://plamo.preferredai.jp/info/plamo-community-license-ja)
  - 個人利用・非商用は基本無料
  - 年商10億円以下の企業も条件付き無料
  - 商用で大規模利用の場合はPFNに要相談
- 辞書データ: 使用するオープンデータ（English Vocabulary Profile等）のライセンスに従う

## ロードマップ

- [x] リポジトリ作成
- [x] 単語モード（多義表示 + CEFRレベル）仕様追加
- [ ] 基本プロジェクト構造 + Compose UI骨格
- [ ] llama.cpp統合 + PLaMoプロンプト実装
- [ ] テキスト翻訳画面完成
- [ ] 単語モードUI（多義カード + CEFRバッジ）
- [ ] オフライン辞書データ準備・バンドル
- [ ] CameraX + ML Kit OCR + テキスト選択UI
- [ ] モデルダウンロードマネージャ
- [ ] 履歴・お気に入り
- [ ] 設定画面・高度なパラメータ
- [ ] GitHub ActionsでAPK自動ビルド
- [ ] リリース用署名APK

## 貢献

Issue / PR歓迎です。特にllama.cppのAndroid最適化やOCRの選択UI改善、辞書データの拡充を歓迎します。

---

Made for offline-first, privacy-respecting translation.
Powered by PLaMo 3 Translate from Preferred Networks.
