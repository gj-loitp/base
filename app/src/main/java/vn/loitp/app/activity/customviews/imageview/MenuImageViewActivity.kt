package vn.loitp.app.activity.customviews.imageview

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.isVisible
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_menu_image_view.*
import vn.loitp.app.R
import vn.loitp.app.activity.customviews.imageview.bigImageView.BigImageViewActivity
import vn.loitp.app.activity.customviews.imageview.bigImageView.BigImageViewWithScrollViewActivity
import vn.loitp.app.activity.customviews.imageview.circleImageView.CircleImageViewActivity
import vn.loitp.app.activity.customviews.imageview.comicView.ComicViewActivity
import vn.loitp.app.activity.customviews.imageview.continuousScrollableImageView.ContinuousScrollableImageViewActivity
import vn.loitp.app.activity.customviews.imageview.fidgetSpinner.FidgetSpinnerImageViewActivity
import vn.loitp.app.activity.customviews.imageview.kenburnView.KenburnViewActivity
import vn.loitp.app.activity.customviews.imageview.panorama.PanoramaImageViewActivity
import vn.loitp.app.activity.customviews.imageview.pinchToZoom.PinchToZoomViewPagerActivity
import vn.loitp.app.activity.customviews.imageview.reflection.ReflectionActivity
import vn.loitp.app.activity.customviews.imageview.roundedImageView.RoundedImageViewActivity
import vn.loitp.app.activity.customviews.imageview.scrollParallax.ScrollParallaxImageViewActivity
import vn.loitp.app.activity.customviews.imageview.stfaiconImageViewer.ListActivity
import vn.loitp.app.activity.customviews.imageview.strectchy.StrectchyImageViewActivity
import vn.loitp.app.activity.customviews.imageview.touch.TouchImageViewActivity
import vn.loitp.app.activity.customviews.imageview.zoom.ZoomImageViewActivity

@LogTag("ImageViewMenuActivity")
@IsFullScreen(false)
class MenuImageViewActivity : BaseFontActivity(), OnClickListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_menu_image_view
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
            this.ivIconRight?.isVisible = false
            this.viewShadow?.isVisible = true
            this.tvTitle?.text = MenuImageViewActivity::class.java.simpleName
        }
        btCirleImageView.setOnClickListener(this)
        btStretchyImageView.setOnClickListener(this)
        btTouchImageView.setOnClickListener(this)
        btZoomImageView.setOnClickListener(this)
        btFidgetSpinner.setOnClickListener(this)
        btContinuousScrollableImageView.setOnClickListener(this)
        btScrollParallaxImageView.setOnClickListener(this)
        btPanoramaImageView.setOnClickListener(this)
        btBigImageView.setOnClickListener(this)
        btBigImageViewWithScrollView.setOnClickListener(this)
        btTouchImageViewWithViewPager.setOnClickListener(this)
        btKenburnView.setOnClickListener(this)
        btComicView.setOnClickListener(this)
        btStfalconImageViewer.setOnClickListener(this)
        btReflection.setOnClickListener(this)
        btRoundedImageView.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            btCirleImageView -> launchActivity(CircleImageViewActivity::class.java)
            btStretchyImageView -> launchActivity(StrectchyImageViewActivity::class.java)
            btTouchImageView -> launchActivity(TouchImageViewActivity::class.java)
            btZoomImageView -> launchActivity(ZoomImageViewActivity::class.java)
            btFidgetSpinner -> launchActivity(FidgetSpinnerImageViewActivity::class.java)
            btContinuousScrollableImageView -> launchActivity(ContinuousScrollableImageViewActivity::class.java)
            btScrollParallaxImageView -> launchActivity(ScrollParallaxImageViewActivity::class.java)
            btPanoramaImageView -> launchActivity(PanoramaImageViewActivity::class.java)
            btBigImageView -> launchActivity(BigImageViewActivity::class.java)
            btBigImageViewWithScrollView -> launchActivity(BigImageViewWithScrollViewActivity::class.java)
            btTouchImageViewWithViewPager -> launchActivity(PinchToZoomViewPagerActivity::class.java)
            btKenburnView -> launchActivity(KenburnViewActivity::class.java)
            btComicView -> launchActivity(ComicViewActivity::class.java)
            btStfalconImageViewer -> launchActivity(ListActivity::class.java)
            btReflection -> launchActivity(ReflectionActivity::class.java)
            btRoundedImageView -> launchActivity(RoundedImageViewActivity::class.java)
        }
    }
}
