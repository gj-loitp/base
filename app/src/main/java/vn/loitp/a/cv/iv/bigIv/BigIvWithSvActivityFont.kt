package vn.loitp.a.cv.iv.bigIv

import android.net.Uri
import android.os.Bundle
import com.github.piasy.biv.view.GlideImageViewFactory
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_big_iv_with_sv.*
import vn.loitp.R
import vn.loitp.common.Constants

// https://github.com/Piasy/BigImageViewer
@LogTag("BigImageViewWithScrollViewActivity")
@IsFullScreen(false)
class BigIvWithSvActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_big_iv_with_sv
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
            this.tvTitle?.text = BigIvWithSvActivityFont::class.java.simpleName
        }

        biv0.setImageViewFactory(GlideImageViewFactory())
        biv1.setImageViewFactory(GlideImageViewFactory())
        biv2.setImageViewFactory(GlideImageViewFactory())
        biv3.setImageViewFactory(GlideImageViewFactory())

        biv0.showImage(
            Uri.parse(Constants.URL_IMG_LARGE_LAND_S),
            Uri.parse(Constants.URL_IMG_LARGE_LAND_O)
        )
        biv1.showImage(Uri.parse(Constants.URL_IMG_LONG))
        biv2.showImage(Uri.parse(Constants.URL_IMG_GIFT))
        biv3.showImage(
            Uri.parse(Constants.URL_IMG_LARGE_PORTRAIT_S),
            Uri.parse(Constants.URL_IMG_LARGE_PORTRAIT_O)
        )
    }
}
