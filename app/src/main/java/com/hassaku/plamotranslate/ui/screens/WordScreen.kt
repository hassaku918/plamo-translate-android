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
import androidx.compose.ui.graphics.Color
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.result != null -> {
                    WordResultCard(entry = uiState.result!!)
                    Text(
                        text = "※ デモ辞書: connoisseur / gut / resilient / ephemeral / pragmatic / ambiguous / meticulous / ubiquitous",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                uiState.notFound -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "「${uiState.query}」は見つかりませんでした",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "単語を入力すると多義・CEFR・例文などが表示されます",
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
            Text(
                text = entry.word,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            entry.phonetic?.let { phonetic ->
                Text(
                    text = phonetic,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                entry.cefr?.let { CefrBadge(level = it) }
                entry.partOfSpeech?.let { PosBadge(pos = it) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "意味：${entry.mainMeaningJa}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            entry.explanationJa?.let { explanation ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = explanation, style = MaterialTheme.typography.bodyLarge)
            }
            if (entry.usages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                SectionTitle(text = "1. 典型的な用法")
                entry.usages.forEach { usage ->
                    UsageItem(en = usage.expression, ja = usage.meaningJa)
                }
            }
            if (entry.exampleSentences.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(text = "2. 例文")
                entry.exampleSentences.forEach { example ->
                    ExampleItem(en = example.english, ja = example.japanese)
                }
            }
            if (entry.synonyms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(text = "3. 類義語との違い")
                entry.synonyms.forEach { synonym ->
                    SynonymRow(
                        word = synonym.word,
                        nuance = synonym.nuanceJa,
                        highlight = synonym.isHighlight
                    )
                }
            }
            entry.usageNotes?.let { notes ->
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(text = "4. 語法上のポイント")
                Text(text = notes, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun CefrBadge(level: String) {
    val color: Color = when {
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
    Column {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun UsageItem(en: String, ja: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "• ", style = MaterialTheme.typography.bodyMedium)
        Column {
            Text(
                text = en,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = ja,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExampleItem(en: String, ja: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = en,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = ja,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
            color = if (highlight) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
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
