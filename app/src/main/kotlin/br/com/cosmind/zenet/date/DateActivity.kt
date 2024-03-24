package br.com.cosmind.zenet.date

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.cosmind.extensions.toFormattedString
import br.com.cosmind.extensions.today
import br.com.khomdrake.imperiya.ui.components.DefaultTopBar
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTheme
import java.time.LocalDate

class DateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImperiyaTheme {
                Scaffold(
                    topBar = {
                        DefaultTopBar(title = "Date Sample") {
                            finish()
                        }
                    }
                ) {
                    DateContent(paddingValues = it)
                }
            }
        }
    }

}