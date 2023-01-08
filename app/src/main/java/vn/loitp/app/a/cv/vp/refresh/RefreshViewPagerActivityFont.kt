package vn.loitp.app.a.cv.vp.refresh

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.Constants
import com.loitp.core.ext.changeTabsFont
import com.loitp.core.ext.setPullLikeIOSHorizontal
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.activity_view_pager_refresh.*
import vn.loitp.R

@LogTag("RefreshViewPagerActivity")
@IsFullScreen(false)
class RefreshViewPagerActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_view_pager_refresh
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = RefreshViewPagerActivityFont::class.java.simpleName
        }
        vp.adapter = SamplePagerAdapter(supportFragmentManager)
        vp.setPullLikeIOSHorizontal()
        tabLayout.setupWithViewPager(vp)
        tabLayout.changeTabsFont(Constants.FONT_PATH)
    }

    @Suppress("DEPRECATION")
    private inner class SamplePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
        override fun getItem(position: Int): Fragment {
            val frmRefresh = FrmRefresh()
            val bundle = Bundle()
            bundle.putInt(FrmRefresh.KEY_POSITION, position)
            frmRefresh.arguments = bundle
            return frmRefresh
        }

        override fun getCount(): Int {
            return 5
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "Page Title $position"
        }
    }
}
