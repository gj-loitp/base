package vn.loitp.app.a.cv.tv.fading

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.utilities.LSocialUtil
import com.loitp.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_fading_text_view.*
import vn.loitp.R

@LogTag("FadingTextViewActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class FadingTextViewActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_fading_text_view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(
                view = this.ivIconLeft,
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.let {
                LUIUtil.setSafeOnClickListenerElastic(
                    view = it,
                    runnable = {
                        LSocialUtil.openUrlInBrowser(
                            context = context,
                            url = "https://github.com/rosenpin/fading-text-view"
                        )
                    }
                )
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.tvTitle?.text = FadingTextViewActivityFont::class.java.simpleName
        }

        val texts = arrayOf(
            "You",
            "can",
            "use",
            "an",
            "array",
            "resource",
            "or a string array",
            "as",
            "the parameter",
        )
        ftv.setTexts(texts)
        ftv.setTimeout(1L, java.util.concurrent.TimeUnit.SECONDS)

    }
}