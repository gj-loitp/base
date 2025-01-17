package com.loitp.views.cal.cosmoCalendar.selection.criteria.month

import com.loitp.views.cal.cosmoCalendar.model.Day
import com.loitp.views.cal.cosmoCalendar.selection.criteria.BaseCriteria
import java.util.*

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
abstract class BaseMonthCriteria : BaseCriteria() {

    protected abstract fun getMonth(): Int
    protected abstract fun getYear(): Int

    override fun isCriteriaPassed(day: Day): Boolean {
        return (day.calendar[Calendar.MONTH] == getMonth()
                && day.calendar[Calendar.YEAR] == getYear())
    }
}
