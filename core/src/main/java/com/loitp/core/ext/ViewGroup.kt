package com.loitp.core.ext

import android.R
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import io.github.inflationx.calligraphy3.CalligraphyUtils

fun <T : View> ViewGroup.getOrNull(index: Int): T? {
    @Suppress("UNCHECKED_CAST") return if (this.childCount > 0 && index in 0 until this.childCount) this[index] as T
    else null
}

fun TabLayout.changeTabsFont(
    fontName: String
) {
    val vg = this.getChildAt(0) as ViewGroup
    val tabsCount = vg.childCount
    for (j in 0 until tabsCount) {
        val vgTab = vg.getChildAt(j) as ViewGroup
        val childCount = vgTab.childCount
        for (i in 0 until childCount) {
            val tabViewChild = vgTab.getChildAt(i)
            if (tabViewChild is TextView) {
                CalligraphyUtils.applyFontToTextView(
                    /* context = */ context,
                    /* textView = */tabViewChild,
                    /* filePath = */fontName
                )
            }
        }
    }
}

@Suppress("unused")
fun NavigationView.setNavMenuItemThemeColors(
    colorDefault: Int,
    color: Int
) {
    // Setting default colors for menu item Text and Icon

    // Defining ColorStateList for menu item Text
    val navMenuTextList = ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_checked),
            intArrayOf(R.attr.state_enabled),
            intArrayOf(R.attr.state_pressed),
            intArrayOf(R.attr.state_focused),
            intArrayOf(R.attr.state_pressed)
        ),
        intArrayOf(color, colorDefault, colorDefault, colorDefault, colorDefault)
    )

    // Defining ColorStateList for menu item Icon
    val navMenuIconList = ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_checked),
            intArrayOf(R.attr.state_enabled),
            intArrayOf(R.attr.state_pressed),
            intArrayOf(R.attr.state_focused),
            intArrayOf(R.attr.state_pressed)
        ),
        intArrayOf(color, colorDefault, colorDefault, colorDefault, colorDefault)
    )
    this.itemTextColor = navMenuTextList
    this.itemIconTintList = navMenuIconList
}
