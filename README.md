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

### 単語モード（独立 + スワイプ対応）★重要
- **別メニューとして完全独立**
- **Grokの会話切り替えのように、左右スワイプで画面を移動可能**
- HorizontalPager を使った滑らかなスワイプ体験
- ボトムナビゲーションとスワイプの両方で切り替え可能

#### 出力スタイル（参考にしたChatGPT共有形式）
```
**connoisseur** /ˌkɒnəˈsɜː(r)/
(C1–C2) 名詞

**意味：目利き、鑑定家、通、愛好家**
ある分野について豊富な知識と経験を持ち、その価値や品質を的確に判断できる人。

1. 典型的な用法
• a connoisseur of art ：美術の目利き
• a wine connoisseur ：ワイン通

2. 例文
He is a connoisseur of Japanese pottery.
彼は日本の陶磁器の目利きだ。

3. 類義語との違い
4. 語法上のポイント
```

- 発音記号 / CEFRバッジ / 品詞
- 主要意味 + 詳細ニュアンス
- 典型的な用法・例文（日英）
- 類義語比較・イディオム

すべて完全オフライン。

### カメラOCR（Lens風）
- CameraX + ML Kit Text Recognition v2
- リアルタイムテキスト検出オーバーレイ
- 検出ブロック/行をタップして選択
- 選択テキストを翻訳 or 単語モードへ送る

### UI / その他機能
- Jetpack Compose + Material 3
- **スワイプ可能なメイン画面切り替え**（Grok風）
- ダーク / ライトテーマ
- 翻訳履歴・単語履歴（独立）
- お気に入り / コピー / 共有 / TTS
- モデルダウンロード管理
- 詳細設定

## ナビゲーション設計（重要）

Grokで質問・画像をスワイプして切り替える感覚に近い体験を目指します。

### メイン画面構成（HorizontalPager）

| ページ | 内容 |
|--------|------|
| 0 | **翻訳**（通常の文章翻訳） |
| 1 | **単語モード**（多義 + CEFR + リッチ辞書UI） |
| 2 | **カメラ**（Lens風OCR） |

- 左右スワイプでスムーズに移動
- 下部にページインジケーター or ボトムナビを併用
- 各ページは独立した状態を保持（翻訳の途中で単語モードに行っても戻れる）

履歴や設定は別途ドロワー or トップバーからアクセス。

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| UI | Jetpack Compose + Material 3 |
| スワイプナビ | Foundation `HorizontalPager` |
| ナビゲーション | Navigation Compose + Pager 連携 |
| アーキテクチャ | MVVM + Clean Architecture |
| 非同期 | Kotlin Coroutines + Flow |
| 推論 | llama.cpp (GGUF) |
| OCR | ML Kit + CameraX |
| 辞書 | Room + バンドルSQLite |
| 永続化 | Room + DataStore |

## 推奨モデル

- [pfnet/plamo-3-translate-0.8b-GGUF](https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF) （Q4_K_M推奨）

## ロードマップ

- [x] リポジトリ作成
- [x] 単語モード仕様（独立メニュー）
- [x] **スワイプ対応ナビゲーション仕様**（Grok風）
- [ ] 基本プロジェクト構造 + HorizontalPager
- [ ] 翻訳画面
- [ ] 単語モード画面（リッチカード）
- [ ] オフライン辞書データ
- [ ] CameraX + OCR
- [ ] 履歴・お気に入り
- [ ] モデル管理
- [ ] GitHub Actions APKビルド

---

Made for offline-first, privacy-respecting translation & vocabulary learning.
Powered by PLaMo 3 Translate from Preferred Networks.
