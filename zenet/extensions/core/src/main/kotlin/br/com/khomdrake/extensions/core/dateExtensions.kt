package br.com.khomdrake.extensions.core

import android.content.Context
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

const val PATTERN_LOCAL_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss"
const val PATTERN_LOCAL_DATE = "yyyy-MM-dd"
fun patternFullDate() = "EEEE, dd '${DateConfig.preposition}' MMMM '${DateConfig.preposition}' yyyy"
fun patternDate() = "dd '${DateConfig.preposition}' MMM '${DateConfig.preposition}' yyyy"
fun patternDate2(isUsLanguage: Boolean = DateConfig.isUsLanguage()) =
    if(isUsLanguage) "MMM/dd/yyyy" else  "dd/MMM/yyyy"
const val PATTERN_LOCAL_DATE_BR = "dd/MM/yyyy"
const val PATTERN_LOCAL_DATE_BR_MONTH_TEXT = "dd/MMMM/yyyy"
const val PATTERN_HOUR_AND_MINUTE = "HH:mm"
const val PATTERN_MONTH_AND_YEAR = "MMM/yyyy"

object DateConfig {

    lateinit var zoneId: ZoneId
    var offsetHours: Int = 0
    lateinit var preposition: String
    lateinit var lang: Locale

    fun setup(context: Context) {
        zoneId = ZoneId.of(context.getString(R.string.extensions_date_zone_id))
//        offsetHours = context.getString(R.string.extensions_date_offset_hours).toInt()
        lang = Locale.forLanguageTag(context.getString(R.string.extensions_date_language))
        preposition = context.getString(R.string.extensions_date_preposition)
    }

    fun isUsLanguage() = lang == Locale.US

}

fun nowAdjustedForZoneId() = LocalDateTime.now(DateConfig.zoneId)

fun today() = LocalDate.now()

fun String.toLocalDate(pattern: String = PATTERN_LOCAL_DATE) =
    LocalDate.from(DateTimeFormatter.ofPattern(pattern).parse(this.split('.').first()))

fun String.toLocalDateTime(pattern: String = PATTERN_LOCAL_DATE_TIME) =
    LocalDateTime.from(DateTimeFormatter.ofPattern(pattern).parse(this.split('.').first()))

fun String.capitalizeCustom() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun LocalDate.toFormattedString(pattern: String = patternFullDate()) =
    format(DateTimeFormatter.ofPattern(pattern, DateConfig.lang)).lowercase().capitalizeCustom()

fun LocalDateTime.toFormattedString(pattern: String = PATTERN_HOUR_AND_MINUTE) =
    format(DateTimeFormatter.ofPattern(pattern, DateConfig.lang)).lowercase().capitalizeCustom()

fun LocalDateTime.differenceFromNow(context: Context) : String {
    val now = nowAdjustedForZoneId().toEpochSecond(ZoneOffset.ofHours(DateConfig.offsetHours))
    val comparing = toEpochSecond(ZoneOffset.ofHours(DateConfig.offsetHours))
    val difference = abs(now - comparing);

    return when {
        difference == 0L -> context.getString(R.string.extensions_update_now)
        difference < 60L -> context.resources.getQuantityString(R.plurals.extensions_update_seconds, difference.toInt(), difference.toInt())
        difference < 3600L -> {
            val quantity = difference.toInt()/60
            context.resources.getQuantityString(R.plurals.extensions_update_minutes, quantity, quantity)
        }
        difference < 86400L -> {
            val quantity = difference.toInt()/3600
            context.resources.getQuantityString(R.plurals.extensions_update_hours, quantity, quantity)
        }
        difference < 2629743L -> {
            val quantity = difference.toInt()/86400
            context.resources.getQuantityString(R.plurals.extensions_update_days, quantity, quantity)
        }
        difference < 31556926L -> {
            val quantity = difference.toInt()/2629743
            context.resources.getQuantityString(R.plurals.extensions_update_months, quantity, quantity)
        }
        difference >= 31556926L -> {
            val quantity = difference.toInt()/31556926
            context.resources.getQuantityString(R.plurals.extensions_update_years, quantity, quantity)
        }
        else -> context.getString(R.string.extensions_when_is_not_possible_to_know_the_difference)
    }
}

fun LocalDate.dayMonthYear() =
    DateTimeFormatter.ofPattern("dd MMM yy", DateConfig.lang).format(this)
