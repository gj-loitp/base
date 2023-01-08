package vn.loitp.a.cv.scratchView.scratchViewIv

import android.annotation.SuppressLint
import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.views.scratch.LScratchImageView
import kotlinx.android.synthetic.main.a_scratch_iv.*
import vn.loitp.R

@LogTag("ScratchViewImageActivity")
@IsFullScreen(false)
class ScratchViewImageActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_scratch_iv
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = ScratchViewImageActivityFont::class.java.simpleName
        }
        scratchImageView.setRevealListener(object : LScratchImageView.IRevealListener {
            @SuppressLint("SetTextI18n")
            override fun onRevealed(iv: LScratchImageView) {
                textView.text = "onRevealed"
            }

            @SuppressLint("SetTextI18n")
            override fun onRevealPercentChangedListener(siv: LScratchImageView, percent: Float) {
                textView.text = "onRevealPercentChangedListener percent: $percent"
            }
        })
    }
}
