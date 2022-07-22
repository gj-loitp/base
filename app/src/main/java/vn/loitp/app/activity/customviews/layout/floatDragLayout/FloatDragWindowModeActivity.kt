package vn.loitp.app.activity.customviews.layout.floatDragLayout

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseActivity
import com.loitpcore.core.utilities.LUIUtil
import com.loitpcore.views.layout.floatDrag.DisplayUtil
import com.loitpcore.views.layout.floatDrag.FloatDragLayout
import kotlinx.android.synthetic.main.activity_0.*
import vn.loitp.app.R

@LogTag("FloatDragWindowModeActivity")
@IsFullScreen(false)
class FloatDragWindowModeActivity : BaseActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(
                view = this.ivIconLeft,
                runnable = {
                    onBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.viewShadow?.isVisible = true
            this.tvTitle?.text = FloatDragWindowModeActivity::class.java.simpleName
        }

        val floatDragLayout = FloatDragLayout(this)
        floatDragLayout.setBackgroundResource(R.drawable.ic_launcher_loitp)
        val size = DisplayUtil.dp2px(this, 45)
        val layoutParams = FrameLayout.LayoutParams(size, size)
        floatDragLayout.layoutParams = layoutParams
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        flWindows.addView(floatDragLayout, layoutParams)

        floatDragLayout.setOnClickListener {
            showShortInformation("Click on the hover and drag buttons")
        }
    }
}
