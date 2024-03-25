package br.com.khomdrake.zenet.request.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.khomdrake.imperiya.ui.components.NormalButton
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTypography
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoCachePage(
    viewModel: NoCacheViewModel = koinViewModel()
) {
    val value by viewModel.valueFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.noCache()
    })

    val listItem = value.data ?: listOf()

    Column(
        Modifier
            .fillMaxSize()
    ) {
        NormalButton(
            name = "Reload data",
            onClick = {
                viewModel.noCache()
            }
        )

        if(value.data != null) {
            LazyColumn(
                content = {
                    items(listItem, key = {item -> item }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                                .animateItemPlacement()
                        ) {
                            Text(
                                text = it,
                                style = ImperiyaTypography.SubTitleBoldStyle,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                },
                contentPadding = PaddingValues(
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
        }
    }

}