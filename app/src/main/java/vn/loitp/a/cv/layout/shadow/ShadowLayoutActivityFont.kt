package vn.loitp.a.cv.layout.shadow

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.openUrlInBrowser
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.core.utils.ConvertUtils
import kotlinx.android.synthetic.main.a_layout_shadow.*
import vn.loitp.R

@LogTag("ShadowLayoutActivity")
@IsFullScreen(false)
class ShadowLayoutActivityFont : BaseActivityFont(), View.OnClickListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_layout_shadow
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
            this.ivIconRight?.let {
                it.setSafeOnClickListenerElastic(
                    runnable = {
                        context.openUrlInBrowser(
                            url = "https://github.com/lijiankun24/ShadowLayout"
                        )
                    }
                )
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.tvTitle?.text = ShadowLayoutActivityFont::class.java.simpleName
        }
        tvChangeOval.setOnClickListener(this)
        tvChangeRadius.setOnClickListener(this)
        tvChangeRectangle.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            tvChangeOval -> slOval.setShadowColor(getColor(R.color.black50))
            tvChangeRadius -> slRectangle.setShadowColor(Color.parseColor("#EE00FF7F"))
            tvChangeRectangle -> slRadius.setShadowRadius(ConvertUtils.dp2px(12f).toFloat())
        }
    }
}
