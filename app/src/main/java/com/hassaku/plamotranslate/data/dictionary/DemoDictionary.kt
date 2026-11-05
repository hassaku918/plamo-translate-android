package com.hassaku.plamotranslate.data.dictionary

import com.hassaku.plamotranslate.data.model.ExampleSentence
import com.hassaku.plamotranslate.data.model.SynonymComparison
import com.hassaku.plamotranslate.data.model.UsageExample
import com.hassaku.plamotranslate.data.model.WordEntry

/**
 * In-memory demo dictionary. Replace with bundled SQLite for production.
 */
object DemoDictionary {

    private val entries = listOf(
        WordEntry(
            word = "connoisseur",
            phonetic = "/ˌkɒnəˈsɜː(r)/",
            cefr = "C1–C2",
            partOfSpeech = "名詞",
            mainMeaningJa = "目利き、鑑定家、通（つう）、愛好家",
            explanationJa = "ある分野について豊富な知識と経験を持ち、その価値や品質を的確に判断できる人。単なる fan よりも専門的な知識や洗練された審美眼を備えているニュアンスが強い。",
            usages = listOf(
                UsageExample("a connoisseur of art", "美術の目利き"),
                UsageExample("a wine connoisseur", "ワイン通"),
                UsageExample("a connoisseur of fine dining", "高級料理に精通した人")
            ),
            exampleSentences = listOf(
                ExampleSentence("He is a connoisseur of Japanese pottery.", "彼は日本の陶磁器の目利きだ。"),
                ExampleSentence("You don't have to be a connoisseur to appreciate this painting.", "この絵画を鑑賞するのに専門知識は必要ない。")
            ),
            synonyms = listOf(
                SynonymComparison("enthusiast", "熱心な愛好家"),
                SynonymComparison("aficionado", "熱狂的な愛好家・通"),
                SynonymComparison("expert", "専門家"),
                SynonymComparison("connoisseur", "審美眼を備えた目利き", isHighlight = true)
            ),
            usageNotes = "可算名詞。connoisseur of A が基本形。洗練された趣味・判断力を含意する。"
        ),
        WordEntry(
            word = "gut",
            phonetic = "/ɡʌt/",
            cefr = "B2–C1",
            partOfSpeech = "名詞・動詞",
            mainMeaningJa = "腸、内臓 / 直感 / 勇気 / 本質",
            explanationJa = "基本イメージは「内臓・腹の中」。そこから直感・勇気・本質などの意味に派生。口語・イディオムで多用。",
            usages = listOf(
                UsageExample("a gut feeling", "直感"),
                UsageExample("have the guts to do", "〜する度胸がある"),
                UsageExample("be gutted", "ひどく落胆する（英）")
            ),
            exampleSentences = listOf(
                ExampleSentence("I have a gut feeling that something is wrong.", "何かがおかしいという直感がある。"),
                ExampleSentence("He didn't have the guts to tell her the truth.", "彼には真実を告げる度胸がなかった。")
            ),
            synonyms = listOf(
                SynonymComparison("instinct", "本能・直感"),
                SynonymComparison("courage", "勇気"),
                SynonymComparison("guts", "度胸・根性（口語）", isHighlight = true)
            ),
            usageNotes = "gut feeling は主観的判断。be gutted は英でよく使う口語。"
        ),
        WordEntry(
            word = "resilient",
            phonetic = "/rɪˈzɪliənt/",
            cefr = "B2–C1",
            partOfSpeech = "形容詞",
            mainMeaningJa = "回復力のある、しなやかな、弾力のある",
            explanationJa = "困難やストレスから素早く立ち直る力があること。人・組織・素材に使える。",
            usages = listOf(
                UsageExample("a resilient person", "打たれ強い人"),
                UsageExample("resilient economy", "回復力のある経済")
            ),
            exampleSentences = listOf(
                ExampleSentence("Children are often more resilient than adults.", "子供は大人より回復力があることが多い。"),
                ExampleSentence("The company proved remarkably resilient during the crisis.", "その会社は危機の間、驚くほどしなやかに持ちこたえた。")
            ),
            synonyms = listOf(
                SynonymComparison("tough", "タフな"),
                SynonymComparison("adaptable", "適応力のある"),
                SynonymComparison("resilient", "回復力・しなやかさに焦点", isHighlight = true)
            ),
            usageNotes = "名詞形は resilience。ビジネス・心理学で頻出。"
        ),
        WordEntry(
            word = "ephemeral",
            phonetic = "/ɪˈfemərəl/",
            cefr = "C1–C2",
            partOfSpeech = "形容詞",
            mainMeaningJa = "はかない、短命の、一瞬の",
            explanationJa = "非常に短い間しか続かないこと。美しさや流行など、すぐに消えてしまうものに使う。",
            usages = listOf(
                UsageExample("ephemeral beauty", "はかない美しさ"),
                UsageExample("ephemeral trends", "一時的な流行")
            ),
            exampleSentences = listOf(
                ExampleSentence("Social media fame can be ephemeral.", "SNSの名声ははかないものになりうる。"),
                ExampleSentence("The ephemeral nature of cherry blossoms is admired in Japan.", "桜のはかなさは日本で愛でられている。")
            ),
            synonyms = listOf(
                SynonymComparison("temporary", "一時的な"),
                SynonymComparison("fleeting", "一瞬の・通り過ぎる"),
                SynonymComparison("ephemeral", "特に美や存在の短さを強調", isHighlight = true)
            ),
            usageNotes = "やや文語的・書き言葉寄り。"
        ),
        WordEntry(
            word = "pragmatic",
            phonetic = "/præɡˈmætɪk/",
            cefr = "B2–C1",
            partOfSpeech = "形容詞",
            mainMeaningJa = "実用的な、現実的な、プラグマティックな",
            explanationJa = "理想よりも実際の効果や結果を重視する態度。問題解決でよく使われる。",
            usages = listOf(
                UsageExample("a pragmatic approach", "現実的なアプローチ"),
                UsageExample("pragmatic solution", "実用的な解決策")
            ),
            exampleSentences = listOf(
                ExampleSentence("We need a pragmatic solution to this problem.", "この問題には現実的な解決策が必要だ。"),
                ExampleSentence("She is pragmatic rather than idealistic.", "彼女は理想主義というより現実的だ。")
            ),
            synonyms = listOf(
                SynonymComparison("practical", "実践的な"),
                SynonymComparison("realistic", "現実的な"),
                SynonymComparison("pragmatic", "効果・結果重視の判断", isHighlight = true)
            ),
            usageNotes = "名詞形 pragmatism。政治・ビジネス文脈で頻出。"
        ),
        WordEntry(
            word = "ambiguous",
            phonetic = "/æmˈbɪɡjuəs/",
            cefr = "B2",
            partOfSpeech = "形容詞",
            mainMeaningJa = "曖昧な、多義的な、はっきりしない",
            explanationJa = "複数の解釈が可能で、意味が一意に定まらないこと。",
            usages = listOf(
                UsageExample("ambiguous statement", "曖昧な発言"),
                UsageExample("ambiguous results", "はっきりしない結果")
            ),
            exampleSentences = listOf(
                ExampleSentence("His answer was deliberately ambiguous.", "彼の答えは意図的に曖昧だった。"),
                ExampleSentence("The contract contains several ambiguous clauses.", "その契約には曖昧な条項がいくつかある。")
            ),
            synonyms = listOf(
                SynonymComparison("vague", "ぼんやりした"),
                SynonymComparison("unclear", "不明瞭な"),
                SynonymComparison("ambiguous", "複数解釈の余地がある", isHighlight = true)
            ),
            usageNotes = "名詞形 ambiguity。言語学・法律でも重要。"
        ),
        WordEntry(
            word = "meticulous",
            phonetic = "/məˈtɪkjələs/",
            cefr = "C1",
            partOfSpeech = "形容詞",
            mainMeaningJa = "細心の、極めて慎重な、几帳面な",
            explanationJa = "細部まで非常に注意深いこと。丁寧すぎるほど正確な作業を指す。",
            usages = listOf(
                UsageExample("meticulous planning", "細心の計画"),
                UsageExample("a meticulous researcher", "極めて慎重な研究者")
            ),
            exampleSentences = listOf(
                ExampleSentence("She kept meticulous records of every experiment.", "彼女はすべての実験を細かく記録していた。"),
                ExampleSentence("The restoration was done with meticulous care.", "修復は細心の注意を払って行われた。")
            ),
            synonyms = listOf(
                SynonymComparison("careful", "注意深い"),
                SynonymComparison("thorough", "徹底的な"),
                SynonymComparison("meticulous", "細部への極端な注意", isHighlight = true)
            ),
            usageNotes = "ポジティブに使われることが多い。"
        ),
        WordEntry(
            word = "ubiquitous",
            phonetic = "/juːˈbɪkwɪtəs/",
            cefr = "C1",
            partOfSpeech = "形容詞",
            mainMeaningJa = "至る所にある、遍在する、ごく一般的な",
            explanationJa = "非常に広く存在し、どこにでも見られること。",
            usages = listOf(
                UsageExample("ubiquitous smartphones", "至る所にあるスマホ"),
                UsageExample("ubiquitous presence", "遍在する存在")
            ),
            exampleSentences = listOf(
                ExampleSentence("Smartphones have become ubiquitous in modern life.", "スマートフォンは現代生活の至る所にある存在になった。"),
                ExampleSentence("Coffee shops are ubiquitous in this city.", "この街ではカフェが至る所にある。")
            ),
            synonyms = listOf(
                SynonymComparison("everywhere", "どこにでも"),
                SynonymComparison("widespread", "広範な"),
                SynonymComparison("ubiquitous", "遍在・どこにでもある", isHighlight = true)
            ),
            usageNotes = "ややフォーマル。テクノロジーや文化の記述でよく使う。"
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
                it.mainMeaningJa.contains(query.trim())
        }
    }

    fun allWords(): List<String> = entries.map { it.word }
}
