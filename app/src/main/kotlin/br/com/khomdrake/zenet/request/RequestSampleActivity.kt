package br.com.khomdrake.zenet.request

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.khomdrake.imperiya.ui.components.DefaultTopBar
import br.com.khomdrake.imperiya.ui.components.TabItem
import br.com.khomdrake.imperiya.ui.components.TabRow
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTheme
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTypography
import br.com.khomdrake.request.data.flow.asResponseStateFlow
import br.com.khomdrake.zenet.R
import br.com.khomdrake.zenet.request.pages.NoCachePage
import br.com.khomdrake.zenet.request.pages.WithCacheDiskPage
import br.com.khomdrake.zenet.request.pages.WithCacheMemoryPage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

enum class RequestPages(val title: Int ) {
    NO_CACHE(R.string.no_cache_title),
    WITH_CACHE_MEMORY(R.string.with_cache_memory),
    WITH_CACHE_DISK(R.string.with_cache_disk)
}

class RequestSampleActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImperiyaTheme {
                Scaffold(
                    topBar = {
                        DefaultTopBar(title = "Request Sample") {
                            finish()
                        }
                    }
                ) { paddingValues ->
                    val items = RequestPages.values().map { stringResource(id = it.title) }
                    val pagerState = rememberPagerState(
                        pageCount = {items.size}
                    )

                    val coroutineScope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValues.calculateTopPadding()
                            )
                    ) {
                        TabRow(
                            itemCount = items.size,
                            currentIndex = pagerState.currentPage,
                            onClick = { newIndex ->
                                coroutineScope.launch {
                                    pagerState.scrollToPage(newIndex)
                                }
                            }
                        ) { index, isSelected ->
                            TabItem(name = items[index], isSelected = isSelected)
                        }

                        HorizontalPager(
                            state = pagerState,
                            pageSpacing = 16.dp,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                            )
                        ) { index ->
                            when(index) {
                                0 -> {
                                    NoCachePage()
                                }
                                1 -> {
                                    WithCacheMemoryPage()
                                }
                                else -> {
                                    WithCacheDiskPage()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}