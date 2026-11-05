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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hassaku.plamotranslate.R
import com.hassaku.plamotranslate.data.model.WordEntry
import com.hassaku.plamotranslate.ui.theme.CefrA1
import com.hassaku.plamotranslate.ui.theme.CefrA2
import com.hassaku.plamotranslate.ui.theme.CefrB1
import com.hassaku.plamotranslate.ui.theme.CefrB2
import com.hassaku.plamotranslate.ui.theme.CefrC1
import com.hassaku.plamotranslate.ui.theme.CefrC2
import com.hassaku.plamotranslate.ui.word.WordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    showTopBar: Boolean = true,
    viewModel: WordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (showTopBar) {
            TopAppBar(title = { Text("単語モード") })
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.word_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = viewModel::clear) {
                            Icon(Icons.Default.Clear, contentDescription = "クリア")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                uiState.result != null -> {
                    WordResultCard(entry = uiState.result!!)
                    Text(
                        text = "※ デモ辞書です。試せる単語: connoisseur / gut / resilient",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                uiState.notFound -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "「${uiState.query}」は見つかりませんでした\n（connoisseur / gut / resilient が使えます）",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "単語を入力すると\n多義・CEFRレベル・例文などが表示されます\n\n試せる単語: connoisseur / gut / resilient",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WordResultCard(entry: WordEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(entry.word, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            entry.phonetic?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                entry.cefr?.let { CefrBadge(it) }
                entry.partOfSpeech?.let { PosBadge(it) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("意味：${entry.mainMeaningJa}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            entry.explanationJa?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }
            if (entry.usages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle("1. 典型的な用法")
                entry.usages.forEach { UsageItem(it.expression, it.meaningJa) }
            }
            if (entry.exampleSentences.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("2. 例文")
                entry.exampleSentences.forEach { ExampleItem(it.english, it.japanese) }
            }
            if (entry.synonyms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("3. 類義語との違い")
                entry.synonyms.forEach { SynonymRow(it.word, it.nuanceJa, it.isHighlight) }
            }
            entry.usageNotes?.let {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle("4. 語法上のポイント")
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable private fun CefrBadge(level: String) {
    val color = when {
        level.contains("A1") -> CefrA1
        level.contains("A2") -> CefrA2
        level.contains("B1") -> CefrB1
        level.contains("B2") -> CefrB2
        level.contains("C1") -> CefrC1
        level.contains("C2") -> CefrC2
        else -> MaterialTheme.colorScheme.primary
    }
    Box(Modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(color.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(level, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable private fun PosBadge(pos: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.secondaryContainer).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(pos, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable private fun UsageItem(en: String, ja: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text("• ", style = MaterialTheme.typography.bodyMedium)
        Column {
            Text(en, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(ja, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable private fun ExampleItem(en: String, ja: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(en, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        Text(ja, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable private fun SynonymRow(word: String, nuance: String, highlight: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(word, style = MaterialTheme.typography.bodyMedium, fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal, color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface, modifier = Modifier.width(110.dp))
        Text(nuance, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
    }
}
