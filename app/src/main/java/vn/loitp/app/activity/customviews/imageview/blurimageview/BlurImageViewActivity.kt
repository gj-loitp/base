package vn.loitp.app.activity.customviews.imageview.blurimageview

import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.LayoutId
import com.annotation.LogTag
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.views.imageview.blur.LBlurImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_imageview_blur.*
import vn.loitp.app.R

@LayoutId(R.layout.activity_imageview_blur)
@LogTag("BlurImageViewActivity")
@IsFullScreen(false)
class BlurImageViewActivity : BaseFontActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
//        method (load) :- load(int resource), load(Bitmap bitmap)
//
//        method(intesity):- intensity( int value) { Increase Blur and limit of value is in between 0 to 25 }
//
//        Synchronous way to Load :- To make blur in synchronous you need to put false in Async method.
//
//        ASynchronous way to Load:- To make blur in asynchronous (Background) you need to put true in Async method.
//
//        Direct get Blur Bitmap :- To get direct blur bitmap call the following code .
//        Bitmap bitmap = LBlurImageView.with(getApplicationContext()).load(R.drawable.mountain).intensity(20).Async(true).getImageBlur();

        //imageView
        LBlurImageView.with(applicationContext)
                .load(R.drawable.iv)
                .intensity(20f)
                .Async(true)
                .into(imageView)

        //imageView2
//        LImageUtil.load(context = this, url = Constants.URL_IMG_5, imageView = imageView2)

        Glide.with(this)
                .load(Constants.URL_IMG)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(imageView2)
    }
}
