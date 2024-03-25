package br.com.khomdrake.zenet.date

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import br.com.khomdrake.imperiya.ui.components.DefaultTopBar
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTheme

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