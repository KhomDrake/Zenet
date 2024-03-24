package br.com.khomdrake.zenet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.khomdrake.zenet.date.DateActivity
import br.com.khomdrake.imperiya.ui.components.DefaultTopBar
import br.com.khomdrake.imperiya.ui.components.NormalButton
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTheme
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImperiyaTheme {
                Scaffold(
                    topBar = {
                        DefaultTopBar(title = getString(R.string.app_name)) {
                            finish()
                        }
                    }
                ) { paddingValues ->
                    SampleContent(
                        paddingValues = PaddingValues(
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            start = 16.dp,
                            bottom = 16.dp,
                            end = 16.dp,
                        ),
                        components = listOf(
                            Component(
                                name = "Dates",
                                kclass = DateActivity::class
                            )
                        ),
                        onClickComponent = { component ->
                            startActivity(
                                Intent(
                                    this,
                                    component.kclass.java
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

class Component(
    val name: String,
    val kclass: KClass<*>
)

@Composable
fun SampleContent(
    paddingValues: PaddingValues,
    components: List<Component>,
    onClickComponent: (Component) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        content = {
            items(components) {
                NormalButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    name = it.name,
                    onClick = { onClickComponent.invoke(it) }
                )
            }
        },
        contentPadding = paddingValues,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
}