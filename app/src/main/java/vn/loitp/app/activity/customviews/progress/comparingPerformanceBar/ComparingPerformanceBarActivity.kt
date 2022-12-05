package vn.loitp.app.activity.customviews.progress.comparingPerformanceBar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LSocialUtil
import com.loitpcore.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_0.*
import kotlinx.android.synthetic.main.activity_0.lActionBar
import kotlinx.android.synthetic.main.activity_comparing_performance_bar.*
import vn.loitp.app.R

@LogTag("ComparingPerformanceBarActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class ComparingPerformanceBarActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_comparing_performance_bar
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
                            url = "https://github.com/cliffgr/ComparingPerformanceBar"
                        )
                    }
                )
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.viewShadow?.isVisible = true
            this.tvTitle?.text = ComparingPerformanceBarActivity::class.java.simpleName
        }

        percentageProgressBar.setProgress(30.0f)
        valuesProgressBar.setValues(100.0f, 20.0f)
        valuesProgressBarPercent.setValues(10.0f, 7.0f)
    }
}