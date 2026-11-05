package com.hassaku.plamotranslate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hassaku.plamotranslate.R
import com.hassaku.plamotranslate.ui.theme.CefrA1
import com.hassaku.plamotranslate.ui.theme.CefrA2
import com.hassaku.plamotranslate.ui.theme.CefrB1
import com.hassaku.plamotranslate.ui.theme.CefrB2
import com.hassaku.plamotranslate.ui.theme.CefrC1
import com.hassaku.plamotranslate.ui.theme.CefrC2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen() {
    var query by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }

    // Demo data for "connoisseur" (matching the ChatGPT share style)
    val demoWord = "connoisseur"
    val demoPhonetic = "/ˌkɒnəˈsɜː(r)/"
    val demoCefr = "C1–C2"
    val demoPos = "名詞"
    val demoMeaning = "目利き、鑑定家、通（つう）、愛好家"
    val demoExplanation =
        "ある分野について豊富な知識と経験を持ち、その価値や品質を的確に判断できる人。単なる fan（ファン・愛好家）よりも、専門的な知識や洗練された審美眼を備えているというニュアンスが強い。"

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("単語モード") }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    showResult = it.isNotBlank()
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.word_hint)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            if (showResult) {
                // Main word card (ChatGPT-style rich layout)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        // Word + phonetic
                        Text(
                            text = if (query.equals(demoWord, ignoreCase = true)) demoWord else query,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = demoPhonetic,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // CEFR + POS badges
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CefrBadge(level = demoCefr)
                            PosBadge(pos = demoPos)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Main meaning
                        Text(
                            text = "意味：$demoMeaning",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = demoExplanation,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Typical usages
                        SectionTitle("1. 典型的な用法")
                        UsageItem("a connoisseur of art", "美術の目利き")
                        UsageItem("a wine connoisseur", "ワイン通")
                        UsageItem("a connoisseur of fine dining", "高級料理に精通した人")
                        UsageItem("a connoisseur of classical music", "クラシック音楽の鑑賞眼を持つ人")

                        Spacer(modifier = Modifier.height(16.dp))

                        // Example sentences
                        SectionTitle("2. 例文")
                        ExampleItem(
                            en = "He is a connoisseur of Japanese pottery.",
                            ja = "彼は日本の陶磁器の目利きだ。"
                        )
                        ExampleItem(
                            en = "You don't have to be a connoisseur to appreciate this painting.",
                            ja = "この絵画を鑑賞するのに、専門的な知識が必要なわけではない。"
                        )
                        ExampleItem(
                            en = "The restaurant attracts connoisseurs from around the world.",
                            ja = "そのレストランには世界中から食通が集まる。"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Synonym comparison
                        SectionTitle("3. 類義語との違い")
                        SynonymRow("enthusiast", "熱心な愛好家")
                        SynonymRow("aficionado", "熱狂的な愛好家・通")
                        SynonymRow("expert", "専門家・熟練者")
                        SynonymRow("connoisseur", "審美眼・鑑識眼を備えた目利き", highlight = true)
                        SynonymRow("critic", "批評家・評論家")

                        Spacer(modifier = Modifier.height(16.dp))

                        // Usage notes
                        SectionTitle("4. 語法上のポイント")
                        Text(
                            text = "• 可算名詞：a connoisseur / connoisseurs\n" +
                                    "• connoisseur of A が基本形\n" +
                                    "• 人の知識だけでなく、洗練された趣味や判断力を含意する\n\n" +
                                    "覚えておきたい表現：a connoisseur of ...（〜の目利き・通）",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = "※ 現在はデモ表示です。オフライン辞書連携後に任意の単語で動作します。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "単語を入力すると\n多義・CEFRレベル・例文などが表示されます",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun CefrBadge(level: String) {
    val color = when {
        level.contains("A1") -> CefrA1
        level.contains("A2") -> CefrA2
        level.contains("B1") -> CefrB1
        level.contains("B2") -> CefrB2
        level.contains("C1") -> CefrC1
        level.contains("C2") -> CefrC2
        else -> MaterialTheme.colorScheme.primary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = level,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PosBadge(pos: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = pos,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun UsageItem(en: String, ja: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", style = MaterialTheme.typography.bodyMedium)
        Column {
            Text(en, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(ja, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ExampleItem(en: String, ja: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(en, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(ja, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SynonymRow(word: String, nuance: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = word,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = nuance,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}
