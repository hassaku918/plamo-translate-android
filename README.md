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

### 単語モード（独立メニュー）★重要
- **別メニューとして独立**（Grokの会話分離のように、翻訳画面とは別の専用画面）
- ボトムナビゲーション or ドロワーから「単語モード」を直接開ける
- 単語を入力するとリッチな辞書風UIで表示

#### 出力スタイル（参考にしたChatGPT共有形式）
以下のような構造で表示します：

```
**connoisseur** /ˌkɒnəˈsɜː(r)/
(C1–C2) 名詞

**意味：目利き、鑑定家、通、愛好家**
ある分野について豊富な知識と経験を持ち、その価値や品質を的確に判断できる人。
単なる fan よりも専門的な知識や洗練された審美眼を備えているニュアンスが強い。

1. 典型的な用法
• a connoisseur of art ：美術の目利き
• a wine connoisseur ：ワイン通
...

2. 例文
He is a connoisseur of Japanese pottery.
彼は日本の陶磁器の目利きだ。

3. 類義語との違い
（表形式で enthusiast / aficionado / expert / connoisseur などを比較）

4. 語法上のポイント・覚えておきたい表現
```

- 発音記号
- CEFRレベルバッジ（A1〜C2）
- 品詞
- 主要意味（日本語）
- 詳細説明
- 典型的な用法リスト
- 例文（英語 + 日本語訳）
- 類義語比較
- イディオム・注意点

すべて**完全オフライン**（バンドル辞書 + 必要に応じてPLaMoで補強）。

### カメラOCR（Lens風）
- CameraX + ML Kit Text Recognition v2
- リアルタイムテキスト検出オーバーレイ
- 検出ブロック/行をタップして選択
- 選択テキストを即座に翻訳 or 単語モードへ送る
- ギャラリー画像からのOCRも対応

### UI / その他機能
- Jetpack Compose + Material 3
- ダーク / ライトテーマ
- 翻訳履歴・単語履歴（それぞれ独立）
- お気に入り
- コピー / 共有 / TTS
- モデルダウンロード管理
- 詳細設定

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| UI | Jetpack Compose + Material 3 |
| ナビゲーション | Navigation Compose（翻訳 / 単語モード / カメラ / 履歴 を分離） |
| アーキテクチャ | MVVM + Clean Architecture |
| 非同期 | Kotlin Coroutines + Flow |
| 推論 | llama.cpp (GGUF) |
| OCR | ML Kit + CameraX |
| 辞書 | Room + バンドルSQLite（多義・CEFR・例文） |
| 永続化 | Room + DataStore |

## ナビゲーション構成（案）

- **翻訳** : 通常の文章翻訳
- **単語** : 専用の単語モード（多義 + CEFR + リッチ説明）
- **カメラ** : Lens風OCR
- **履歴** : 翻訳履歴 / 単語履歴
- **設定**

単語モードは翻訳画面とは完全に分離した「別の会話空間」のように使えます。

## 推奨モデル

- [pfnet/plamo-3-translate-0.8b-GGUF](https://huggingface.co/pfnet/plamo-3-translate-0.8b-GGUF) （Q4_K_M推奨）

## ライセンス注意

- アプリコード: MIT（予定）
- PLaMo 3 Translate: PLaMo Community License
- 辞書データ: 使用オープンデータのライセンスに従う

## ロードマップ

- [x] リポジトリ作成
- [x] 単語モード仕様（独立メニュー + リッチUI）
- [ ] 基本プロジェクト構造 + Navigation
- [ ] 翻訳画面
- [ ] 単語モード画面（ChatGPT風リッチカード）
- [ ] オフライン辞書データ準備
- [ ] CameraX + OCR
- [ ] 履歴・お気に入り
- [ ] モデル管理
- [ ] GitHub Actions APKビルド

---

Made for offline-first, privacy-respecting translation & vocabulary learning.
Powered by PLaMo 3 Translate from Preferred Networks.
