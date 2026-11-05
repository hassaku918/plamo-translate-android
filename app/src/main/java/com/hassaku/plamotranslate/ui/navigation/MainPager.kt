package com.hassaku.plamotranslate.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.hassaku.plamotranslate.R
import com.hassaku.plamotranslate.ui.screens.CameraScreen
import com.hassaku.plamotranslate.ui.screens.HistoryScreen
import com.hassaku.plamotranslate.ui.screens.SettingsScreen
import com.hassaku.plamotranslate.ui.screens.TranslateScreen
import com.hassaku.plamotranslate.ui.screens.WordScreen
import kotlinx.coroutines.launch

enum class MainTab(
    val titleRes: Int,
    val icon: ImageVector
) {
    TRANSLATE(R.string.tab_translate, Icons.Default.Translate),
    WORD(R.string.tab_word, Icons.Default.MenuBook),
    CAMERA(R.string.tab_camera, Icons.Default.CameraAlt)
}

enum class OverlayScreen {
    NONE, HISTORY, SETTINGS
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainPagerScreen() {
    val tabs = MainTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()
    var overlay by remember { mutableStateOf(OverlayScreen.NONE) }

    when (overlay) {
        OverlayScreen.HISTORY -> {
            HistoryScreen(onBack = { overlay = OverlayScreen.NONE })
            return
        }
        OverlayScreen.SETTINGS -> {
            SettingsScreen(onBack = { overlay = OverlayScreen.NONE })
            return
        }
        OverlayScreen.NONE -> { /* main content */ }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (tabs.getOrNull(pagerState.currentPage)) {
                            MainTab.TRANSLATE -> "PLaMo Translate"
                            MainTab.WORD -> "単語モード"
                            MainTab.CAMERA -> "カメラ OCR"
                            null -> "PLaMo Translate"
                        }
                    )
                },
                actions = {
                    IconButton(onClick = { overlay = OverlayScreen.HISTORY }) {
                        Icon(Icons.Default.History, contentDescription = "履歴")
                    }
                    IconButton(onClick = { overlay = OverlayScreen.SETTINGS }) {
                        Icon(Icons.Default.Settings, contentDescription = "設定")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(tab.titleRes)
                            )
                        },
                        label = { Text(text = stringResource(tab.titleRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            beyondViewportPageCount = 1
        ) { page ->
            when (tabs[page]) {
                MainTab.TRANSLATE -> TranslateScreen(showTopBar = false)
                MainTab.WORD -> WordScreen(showTopBar = false)
                MainTab.CAMERA -> CameraScreen()
            }
        }
    }
}
