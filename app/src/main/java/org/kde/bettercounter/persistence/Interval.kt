package org.kde.bettercounter.persistence

import android.content.Context
import org.kde.bettercounter.R
import org.kde.bettercounter.extensions.toCalendar
import org.kde.bettercounter.extensions.toZonedDateTime
import org.kde.bettercounter.extensions.truncate
import java.time.temporal.ChronoUnit
import java.util.*

enum class Interval(val humanReadableResource : Int) {
    DAY(R.string.interval_day),
    WEEK(R.string.interval_week),
    MONTH(R.string.interval_month),
    YEAR(R.string.interval_year),
    LIFETIME(R.string.interval_lifetime);

    companion object {
        fun humanReadableValues(context : Context): List<String> {
            return values().map { context.getString(it.humanReadableResource) }
        }
    }

    // Rounds up. Both dates included. Eg: returns 2 weeks from Monday at 00:00 to next Monday at 00:00
    fun count(from : Date, to : Date) : Int {
        val count = when (this) {
            DAY -> {
                val dayTruncatedFrom = from.toCalendar().apply { truncate(Calendar.DAY_OF_MONTH) }.time.toZonedDateTime()
                val dayTruncatedTo = to.toCalendar().apply { truncate(Calendar.DAY_OF_MONTH) }.time.toZonedDateTime()
                ChronoUnit.DAYS.between(dayTruncatedFrom, dayTruncatedTo).toInt()
            }
            WEEK -> {
                val weekTruncatedFrom = from.toCalendar().apply {truncate(Calendar.WEEK_OF_YEAR) }.time.toZonedDateTime()
                val weekTruncatedTo = to.toCalendar().apply { truncate(Calendar.WEEK_OF_YEAR) }.time.toZonedDateTime()
                ChronoUnit.WEEKS.between(weekTruncatedFrom, weekTruncatedTo).toInt()
            }
            MONTH -> (to.month - from.month) + (to.year - from.year)*12
            YEAR -> to.year - from.year
            LIFETIME -> 0
        }
        //Log.e("Interval", "${count+1} $this between ${from.toCalendar().toSimpleDateString()} and ${to.toCalendar().toSimpleDateString()}")
        return count + 1
    }

}

val DEFAULT_INTERVAL = Interval.LIFETIME
