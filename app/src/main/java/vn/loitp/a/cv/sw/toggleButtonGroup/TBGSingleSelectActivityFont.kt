package vn.loitp.a.cv.sw.toggleButtonGroup

import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_switch_tbg_single.*
import vn.loitp.R

@LogTag("TBGSingleSelectActivity")
@IsFullScreen(false)
class TBGSingleSelectActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_switch_tbg_single
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
            this.tvTitle?.text = TBGSingleSelectActivityFont::class.java.simpleName
        }
        groupChoices.setOnCheckedChangeListener { _, checkedId ->
            logD("onCheckedChanged(): checkedId = $checkedId")
            showShortInformation("onCheckedChanged(): checkedId = $checkedId")
        }
    }
}
