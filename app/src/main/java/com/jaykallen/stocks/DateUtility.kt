package com.jaykallen.stocks

import android.text.format.DateUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {
    companion object {

        fun formatIso2West(input: String): String {
            // converts from 2017-07-17 to 7/17/17
            try {
                val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val westDateFormat = SimpleDateFormat("M/dd", Locale.US)
                return westDateFormat.format(isoDateFormat.parse(input))
            } catch (e: Exception) {
                return ""
            }
        }

        fun daysBwDates(laterDate: Date, earlierDate: Date): Long? {
            return (laterDate.time - earlierDate.time) / DateUtils.DAY_IN_MILLIS
        }

        fun stringToDate(dateString: String): Date {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return try {
                sdf.parse(dateString)
            } catch (e: Exception) {
                sdf.parse("2019-03-20")
            }
        }

    }
}