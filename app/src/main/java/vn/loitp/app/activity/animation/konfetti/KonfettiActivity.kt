package vn.loitp.app.activity.animation.konfetti

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.ext.setSafeOnClickListener
import com.loitpcore.core.utilities.LSocialUtil
import com.loitpcore.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_0.lActionBar
import kotlinx.android.synthetic.main.activity_konfetti.*
import vn.loitp.app.R

@LogTag("KonfettiActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class KonfettiActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_konfetti
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
                            url = "https://github.com/DanielMartinus/konfetti"
                        )
                    }
                )
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.viewShadow?.isVisible = true
            this.tvTitle?.text = KonfettiActivity::class.java.simpleName
        }

        btnFestive.setSafeOnClickListener {
            konfettiView.start(Presets.festive())
        }
        btnExplode.setSafeOnClickListener {
            konfettiView.start(Presets.explode())
        }
        btnParade.setSafeOnClickListener {
            konfettiView.start(Presets.parade())
        }
        btnRain.setSafeOnClickListener {
            konfettiView.start(Presets.rain())
        }
    }
}