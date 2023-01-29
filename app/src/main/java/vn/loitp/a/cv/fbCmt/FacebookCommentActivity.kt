package vn.loitp.a.cv.fbCmt

import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.openFacebookComment
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_facebook_comment.*
import vn.loitp.R

@LogTag("FacebookCommentActivity")
@IsFullScreen(false)
class FacebookCommentActivity : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_facebook_comment
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
            this.tvTitle?.text = FacebookCommentActivity::class.java.simpleName
        }
        bt.setSafeOnClickListener {
            this.openFacebookComment(
                url = "http://truyentuan.com/one-piece-chuong-907/",
            )
        }
    }
}