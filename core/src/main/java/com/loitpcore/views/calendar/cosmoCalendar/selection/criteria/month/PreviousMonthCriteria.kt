package com.loitpcore.views.calendar.cosmoCalendar.selection.criteria.month

import com.loitpcore.views.calendar.cosmoCalendar.utils.DateUtils.getCalendar
import java.util.*

class PreviousMonthCriteria : BaseMonthCriteria() {

    private val currentTimeInMillis: Long = System.currentTimeMillis()

    override fun getMonth(): Int {
        val calendar = getCalendar(currentTimeInMillis)
        calendar.add(Calendar.MONTH, -1)
        return calendar[Calendar.MONTH]
    }

    override fun getYear(): Int {
        val calendar = getCalendar(currentTimeInMillis)
        calendar.add(Calendar.MONTH, -1)
        return calendar[Calendar.YEAR]
    }

}