package vn.loitp.a.cv.scratchView.scratchViewTv

import android.annotation.SuppressLint
import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.views.scratch.LScratchTextView
import kotlinx.android.synthetic.main.a_scratch_tv.*
import vn.loitp.R

@LogTag("ScratchViewTextActivity")
@IsFullScreen(false)
class ScratchViewTextActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_scratch_tv
    }

    @SuppressLint("SetTextI18n")
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
            this.tvTitle?.text = ScratchViewTextActivityFont::class.java.simpleName
        }

        scratchViewTextView.setRevealListener(object : LScratchTextView.IRevealListener {

            @SuppressLint("SetTextI18n")
            override fun onRevealed(tv: LScratchTextView) {
                textView.text = "onRevealed"
            }

            @SuppressLint("SetTextI18n")
            override fun onRevealPercentChangedListener(stv: LScratchTextView, percent: Float) {
                textView.text = "onRevealPercentChangedListener percent: $percent"
            }
        })
    }
}
