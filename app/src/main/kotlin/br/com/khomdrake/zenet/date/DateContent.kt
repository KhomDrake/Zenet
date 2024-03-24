package br.com.khomdrake.zenet.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.khomdrake.extensions.differenceFromNow
import br.com.khomdrake.extensions.nowAdjustedForZoneId
import br.com.khomdrake.extensions.patternDate2
import br.com.khomdrake.extensions.toFormattedString
import br.com.khomdrake.extensions.today
import br.com.khomdrake.imperiya.ui.preview.BackgroundPreview
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTheme
import br.com.khomdrake.imperiya.ui.theme.ImperiyaTypography
import java.time.LocalDate
import java.time.Month

class DateItem(
    val name: String,
    val value: String
)

@Composable
fun DateContent(paddingValues: PaddingValues) {
    val context = LocalContext.current

    val items = listOf<DateItem>(
        DateItem(
            "Today:",
            today().toFormattedString()
        ),
        DateItem(
            "Specific date 14-01-2020 Full Date",
            LocalDate.of(2020, Month.JANUARY, 14)
                .toFormattedString()
        ),
        DateItem(
            "Specific date 14-01-2020 Pattern date US",
            LocalDate.of(2020, Month.JANUARY, 14)
                .toFormattedString(patternDate2(isUsLanguage = true))
        ),
        DateItem(
            "Specific date 14-01-2020 Pattern date default",
            LocalDate.of(2020, Month.JANUARY, 14)
                .toFormattedString(patternDate2(isUsLanguage = false))
        ),
        DateItem(
            "LocalDateTime Pattern hour and minute",
            nowAdjustedForZoneId()
                .toFormattedString()
        ),
        DateItem(
            "Diffence from now - Minutes",
            nowAdjustedForZoneId()
                .minusMinutes(20)
                .differenceFromNow(context)
        ),
        DateItem(
            "Diffence from now - Hours",
            nowAdjustedForZoneId()
                .minusHours(7)
                .minusMinutes(25)
                .differenceFromNow(context)
        ),
        DateItem(
            "Diffence from now - Days",
            nowAdjustedForZoneId()
                .minusDays(15)
                .minusHours(7)
                .minusMinutes(25)
                .differenceFromNow(context)
        ),
        DateItem(
            "Diffence from now - Months",
            nowAdjustedForZoneId()
                .minusDays(15)
                .minusHours(7)
                .minusMinutes(25)
                .minusMonths(5)
                .differenceFromNow(context)
        ),
        DateItem(
            "Diffence from now - Years",
            nowAdjustedForZoneId()
                .minusDays(15)
                .minusHours(7)
                .minusMinutes(25)
                .minusMonths(5)
                .minusYears(5)
                .differenceFromNow(context)
        )
    )

    LazyColumn(
        content = {
            items(items) {
                DateItem(
                    dateItem = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 16.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
}

@Composable
fun DateItem(
    dateItem: DateItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = dateItem.name,
            modifier = Modifier
                .fillMaxWidth(),
            style = ImperiyaTypography.SubTitleBoldStyle,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = dateItem.value,
            modifier = Modifier
                .fillMaxWidth(),
            style = ImperiyaTypography.ParagraphBoldStyle,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@PreviewLightDark
@Composable
fun DateItemPreview() {
    ImperiyaTheme {
        BackgroundPreview {
            DateItem(
                dateItem = DateItem(
                "Specific date 14-01-2020 Pattern date default",
                    "14/01/2020"
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
