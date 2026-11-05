package com.hassaku.plamotranslate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.hassaku.plamotranslate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen() {
    var sourceText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var sourceLang by remember { mutableStateOf("English") }
    var targetLang by remember { mutableStateOf("Japanese") }
    var isTranslating by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("PLaMo Translate") }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Language selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sourceLang,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = {
                    val tmp = sourceLang
                    sourceLang = targetLang
                    targetLang = tmp
                    // also swap texts
                    val tmpText = sourceText
                    sourceText = translatedText
                    translatedText = tmpText
                }) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "言語を入れ替え")
                }
                Text(
                    text = targetLang,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Source input
            OutlinedTextField(
                value = sourceText,
                onValueChange = { sourceText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                placeholder = { Text(stringResource(R.string.translate_hint)) },
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    if (sourceText.isNotEmpty()) {
                        IconButton(onClick = { sourceText = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "クリア")
                        }
                    }
                }
            )

            Button(
                onClick = {
                    // TODO: call PLaMo inference
                    isTranslating = true
                    // placeholder
                    translatedText = "（ここにPLaMoの翻訳結果が表示されます）\n\n入力: $sourceText"
                    isTranslating = false
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = sourceText.isNotBlank() && !isTranslating
            ) {
                Text(if (isTranslating) "翻訳中…" else "翻訳する")
            }

            // Result
            if (translatedText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "翻訳結果",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = {
                                clipboardManager.setText(AnnotatedString(translatedText))
                            }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "コピー")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = translatedText,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "※ 現在はプレースホルダーです。PLaMoモデル連携後に本格動作します。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
