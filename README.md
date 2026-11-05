# PLaMo Translate Android

**完全オフライン**で動く高品質翻訳アプリ。Preferred Networksの **PLaMo 3 Translate 0.8B** をオンデバイスで動作させ、Google Lens風のカメラテキスト選択にも対応。

## 現在の進捗（2026-11-05）

### 実装済み ✅
- [x] プロジェクト骨格（Gradle + Compose + Hilt + Room）
- [x] **HorizontalPager スワイプ切り替え**（翻訳 ⇄ 単語 ⇄ カメラ）
- [x] ボトムナビゲーション連動
- [x] 翻訳画面 + TranslateViewModel（常体/敬体スタイル選択付き）
- [x] **単語モード**（ChatGPT風リッチ辞書UI + ViewModel）
- [x] デモ辞書（connoisseur / gut / resilient）
- [x] **履歴機能**（翻訳履歴・単語履歴・お気に入り）
- [x] Room DB + Repository 層
- [x] DI（Hilt）
- [x] Adaptive Icon

### これから
- [ ] PLaMo (llama.cpp) 推論連携
- [ ] 本格オフライン辞書（SQLiteバンドル）
- [ ] CameraX + ML Kit OCR 実装
- [ ] 履歴画面への導線（TopBarメニュー等）
- [ ] モデルダウンロード管理
- [ ] GitHub Actions でAPK自動ビルド

## 試し方

```bash
git clone https://github.com/hassaku918/plamo-translate-android.git
```

Android Studioで開いて Run。

### 単語モードで試せる単語
- `connoisseur`
- `gut`
- `resilient`

### 操作
- **左右スワイプ** で画面切り替え（Grok風）
- 下部ナビをタップしても移動可能
- 翻訳すると履歴に自動保存
- 単語を調べると単語履歴に自動保存

## 技術スタック

| レイヤー | 技術 |
|---------|------|
| UI | Jetpack Compose + Material 3 |
| スワイプ | HorizontalPager |
| DI | Hilt |
| DB | Room |
| OCR予定 | CameraX + ML Kit |
| 推論予定 | llama.cpp (GGUF) |

## ライセンス

- アプリコード: MIT（予定）
- PLaMo 3 Translate: PLaMo Community License

---

Made for offline-first, privacy-respecting translation & vocabulary learning.
