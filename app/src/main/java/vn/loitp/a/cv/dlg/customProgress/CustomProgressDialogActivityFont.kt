package vn.loitp.a.cv.dlg.customProgress

import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setDelay
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_dlg_custom_progress.*
import vn.loitp.R

@LogTag("CustomProgressDialogActivity")
@IsFullScreen(false)
class CustomProgressDialogActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_dlg_custom_progress
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
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = CustomProgressDialogActivityFont::class.java.simpleName
        }
        bt0.setSafeOnClickListener {
            showDialogProgress()
            setDelay(mls = 4000, runnable = {
                hideDialogProgress()
            })
        }
    }
}
