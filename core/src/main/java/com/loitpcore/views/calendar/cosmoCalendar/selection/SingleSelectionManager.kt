package com.loitpcore.views.calendar.cosmoCalendar.selection

import com.loitpcore.views.calendar.cosmoCalendar.model.Day

class SingleSelectionManager(
    onDaySelectedListener: OnDaySelectedListener?
) : BaseSelectionManager() {

    init {
        this.onDaySelectedListener = onDaySelectedListener
    }

    private var day: Day? = null

    override fun toggleDay(day: Day) {
        this.day = day
        onDaySelectedListener?.onDaySelected()
    }

    override fun isDaySelected(day: Day): Boolean {
        return day == this.day
    }

    override fun clearSelections() {
        day = null
    }
}