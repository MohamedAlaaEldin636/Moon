package app.grand.tafwak.domain.utils

import app.grand.tafwak.domain.utils.DateUtils.Companion.API_DATE_FORMAT
import app.grand.tafwak.domain.utils.DateUtils.Companion.DAY_NAME
import app.grand.tafwak.domain.utils.DateUtils.Companion.TIME_12_FORMAT
import app.grand.tafwak.domain.utils.DateUtils.Companion.TIME_24_FORMAT
import java.text.SimpleDateFormat
import java.util.*


class DateUtils {
  companion object {
    const val API_DATE_FORMAT = "yyyy-MM-dd"
    const val UI_DATE_FORMAT = "dd-MM-yyyy"
    const val FULL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val MONTH_UI_DATE_FORMAT = "dd MMM yyyy"
    const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val TIME_12_FORMAT = "hh:mm:ss aa"
    const val TIME_24_FORMAT = "HH:mm"
    const val TIME_24_FORMAT_WITH_SECONDS = "HH:mm:ss"
    const val TIME_HOUR_ONLY = "HH"
    const val TIME_MINUTE_ONLY = "mm"
    const val DATE_TIME_12_FORMAT = "yyyy-MM-dd hh:mm a"
    const val DATE_TIME_UI_FORMAT = "dd MMM yyyy, hh:mm a"
    const val DAY_NAME = "EEEE"
    const val SHORT_DAY_NAME = "E"
    const val DATE_WITH_DAY_NAME = "EEE, dd MMM yyyy"
    const val HOUR = "HH"
    const val MINUTE = "mm"
    const val DAY = "dd"
    const val MONTH = "MM"
    const val YEAR = "yyyy"
  }
}

fun String.dayName(parsedDate: String): String {
  val inputFormat = SimpleDateFormat(API_DATE_FORMAT, getLocale())
  val dayFormat = SimpleDateFormat(DAY_NAME, getLocale())
  val date: Date?
  return try {
    date = inputFormat.parse(parsedDate)
    return dayFormat.format(date)
  } catch (e: Exception) {
    e.printStackTrace()
    ""
  }
}

fun String.time(parsedTime: String): String {
  val time24Format = SimpleDateFormat(TIME_24_FORMAT, getLocale())
  val time12Format = SimpleDateFormat(TIME_12_FORMAT, getLocale())
  val date: Date?
  return try {
    date = time24Format.parse(parsedTime)
    return time12Format.format(date)
  } catch (e: Exception) {
    e.printStackTrace()
    ""
  }
}

fun getLocale(): Locale? {
  return when (Locale.getDefault().language) {
    "en" -> {
      Locale.ENGLISH
    }
    "ar" -> {
      Locale("ar")
    }
    "fr" -> {
      Locale.FRENCH
    }
    else -> {
      Locale.ENGLISH
    }
  }
}