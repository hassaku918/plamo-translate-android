package com.hassaku.plamotranslate.data.dictionary

import com.hassaku.plamotranslate.data.model.ExampleSentence
import com.hassaku.plamotranslate.data.model.SynonymComparison
import com.hassaku.plamotranslate.data.model.UsageExample
import com.hassaku.plamotranslate.data.model.WordEntry

/**
 * Temporary in-memory demo dictionary.
 * Will be replaced by bundled SQLite later.
 */
object DemoDictionary {

    private val entries = listOf(
        WordEntry(
            word = "connoisseur",
            phonetic = "/ˌkɒnəˈsɜː(r)/",
            cefr = "C1–C2",
            partOfSpeech = "名詞",
            mainMeaningJa = "目利き、鑑定家、通（つう）、愛好家",
            explanationJa = "ある分野について豊富な知識と経験を持ち、その価値や品質を的確に判断できる人。単なる fan（ファン・愛好家）よりも、専門的な知識や洗練された審美眼を備えているというニュアンスが強い。",
            usages = listOf(
                UsageExample("a connoisseur of art", "美術の目利き"),
                UsageExample("a wine connoisseur", "ワイン通"),
                UsageExample("a connoisseur of fine dining", "高級料理に精通した人"),
                UsageExample("a connoisseur of classical music", "クラシック音楽の鑑賞眼を持つ人")
            ),
            exampleSentences = listOf(
                ExampleSentence(
                    "He is a connoisseur of Japanese pottery.",
                    "彼は日本の陶磁器の目利きだ。"
                ),
                ExampleSentence(
                    "You don't have to be a connoisseur to appreciate this painting.",
                    "この絵画を鑑賞するのに、専門的な知識が必要なわけではない。"
                ),
                ExampleSentence(
                    "The restaurant attracts connoisseurs from around the world.",
                    "そのレストランには世界中から食通が集まる。"
                )
            ),
            synonyms = listOf(
                SynonymComparison("enthusiast", "熱心な愛好家"),
                SynonymComparison("aficionado", "熱狂的な愛好家・通"),
                SynonymComparison("expert", "専門家・熟練者"),
                SynonymComparison("connoisseur", "審美眼・鑑識眼を備えた目利き", isHighlight = true),
                SynonymComparison("critic", "批評家・評論家")
            ),
            usageNotes = "• 可算名詞：a connoisseur / connoisseurs\n• connoisseur of A が基本形\n• 人の知識だけでなく、洗練された趣味や判断力を含意する\n\n覚えておきたい表現：a connoisseur of ...（〜の目利き・通）"
        ),
        WordEntry(
            word = "gut",
            phonetic = "/ɡʌt/",
            cefr = "B2–C1",
            partOfSpeech = "名詞・動詞・形容詞",
            mainMeaningJa = "腸、内臓 / 直感 / 勇気 / 本質",
            explanationJa = "基本イメージは「内臓・腹の中」。そこから「直感」「勇気」「本質」などの意味に派生する。口語表現・イディオムで非常によく使われる。",
            usages = listOf(
                UsageExample("a gut feeling", "直感"),
                UsageExample("a gut instinct", "本能的な直感"),
                UsageExample("have the guts to do", "〜する度胸がある"),
                UsageExample("be gutted", "ひどく落胆する（主に英）")
            ),
            exampleSentences = listOf(
                ExampleSentence(
                    "I have a gut feeling that something is wrong.",
                    "何かがおかしいという直感がある。"
                ),
                ExampleSentence(
                    "He didn't have the guts to tell her the truth.",
                    "彼には彼女に真実を告げる度胸がなかった。"
                ),
                ExampleSentence(
                    "I was absolutely gutted when I heard the news.",
                    "その知らせを聞いて本当に落胆した。"
                )
            ),
            synonyms = listOf(
                SynonymComparison("instinct", "本能・直感"),
                SynonymComparison("intuition", "直感（比較的中立）"),
                SynonymComparison("courage", "勇気"),
                SynonymComparison("guts", "度胸・根性（口語）", isHighlight = true)
            ),
            usageNotes = "• gut feeling は個人的・主観的な判断を表す\n• be gutted はイギリス英語で非常によく使われる口語表現\n• 動詞としては「内臓を取り除く」「内部を破壊する」の意味もある"
        ),
        WordEntry(
            word = "resilient",
            phonetic = "/rɪˈzɪliənt/",
            cefr = "B2–C1",
            partOfSpeech = "形容詞",
            mainMeaningJa = "回復力のある、しなやかな、弾力のある",
            explanationJa = "困難やストレスから素早く立ち直る力があること。人・組織・素材などに使える。",
            usages = listOf(
                UsageExample("a resilient person", "打たれ強い人"),
                UsageExample("resilient economy", "回復力のある経済"),
                UsageExample("emotionally resilient", "精神的に回復力がある")
            ),
            exampleSentences = listOf(
                ExampleSentence(
                    "Children are often more resilient than adults.",
                    "子供は大人よりも回復力があることが多い。"
                ),
                ExampleSentence(
                    "The company proved remarkably resilient during the crisis.",
                    "その会社は危機の間、驚くほどしなやかに持ちこたえた。"
                )
            ),
            synonyms = listOf(
                SynonymComparison("tough", "タフな・頑丈な"),
                SynonymComparison("strong", "強い"),
                SynonymComparison("adaptable", "適応力のある"),
                SynonymComparison("resilient", "回復力・しなやかさに焦点", isHighlight = true)
            ),
            usageNotes = "名詞形は resilience（回復力・弾力性）。ビジネスや心理学の文脈でよく使われる。"
        )
    )

    fun lookup(query: String): WordEntry? {
        val normalized = query.trim().lowercase()
        return entries.find { it.word.lowercase() == normalized }
    }

    fun search(query: String): List<WordEntry> {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return emptyList()
        return entries.filter {
            it.word.lowercase().contains(normalized) ||
                    it.mainMeaningJa.contains(normalized)
        }
    }
}
