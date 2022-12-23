package vn.loitp.app.a.cv.tv.typeWrite

import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseFontActivity
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_text_view_type_writer.*
import vn.loitp.R

@LogTag("TypeWriterTextViewActivity")
@IsFullScreen(false)
class TypeWriterTextViewActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_text_view_type_writer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(
                view = this.ivIconLeft,
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = TypeWriterTextViewActivity::class.java.simpleName
        }
        btn.setSafeOnClickListener {
            textView.text = ""
            textView.setCharacterDelay(150)
            textView.animateText("Type Writer Effect")
        }
    }
}