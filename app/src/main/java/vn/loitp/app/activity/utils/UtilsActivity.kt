package vn.loitp.app.activity.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LUIUtil
import com.utils.util.ActivityUtils
import kotlinx.android.synthetic.main.activity_utils.*
import vn.loitp.app.R
import java.util.*
import kotlin.collections.ArrayList

class UtilsActivity : BaseFontActivity() {

    private val listClass = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager.adapter = SlidePagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        LUIUtil.changeTabsFont(tabLayout, Constants.FONT_PATH)

        setupData()
    }

    private fun setupData() {
        listClass.add(ActivityUtils::class.java.simpleName)
        viewPager.adapter?.notifyDataSetChanged()
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String {
        return javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_utils
    }

    private inner class SlidePagerAdapter internal constructor(fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            if (listClass[position] == ActivityUtils::class.java.simpleName) {
                return FrmActivityUtils.newInstance()
            }
            return FrmActivityUtils.newInstance()
        }

        override fun getCount(): Int {
            return listClass.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listClass[position].toLowerCase(Locale.getDefault())
        }
    }
}
