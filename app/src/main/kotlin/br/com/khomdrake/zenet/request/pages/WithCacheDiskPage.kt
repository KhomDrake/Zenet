package br.com.khomdrake.zenet.request.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun WithCacheDiskPage(viewModel: WithCacheDiskViewModel = koinViewModel()) {
    val value by viewModel.valueFlow.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.withCache()
    })

    Column(
        Modifier
            .fillMaxSize()
    ) {
        NormalButton(
            name = "Reload data",
            onClick = {
                viewModel.withCache()
            }
        )

        Text(
            text = "Cache time is 2 minutes",
            style = ImperiyaTypography.TitleBigStyle,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        value.data?.let { data ->
            Text(
                text = "Value: $data",
                style = ImperiyaTypography.SubTitleBoldStyle,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}