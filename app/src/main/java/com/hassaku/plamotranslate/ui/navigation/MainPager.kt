package com.hassaku.plamotranslate.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.hassaku.plamotranslate.R
import com.hassaku.plamotranslate.ui.screens.CameraScreen
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPagerScreen() {
    val tabs = MainTab.entries
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(tab.titleRes)
                            )
                        },
                        label = {
                            Text(text = stringResource(tab.titleRes))
                        }
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
                MainTab.TRANSLATE -> TranslateScreen()
                MainTab.WORD -> WordScreen()
                MainTab.CAMERA -> CameraScreen()
            }
        }
    }
}
