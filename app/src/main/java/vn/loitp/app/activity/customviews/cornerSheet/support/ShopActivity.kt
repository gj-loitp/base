package vn.loitp.app.activity.customviews.cornerSheet.support

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import vn.loitp.app.R
import vn.loitp.app.activity.customviews.cornerSheet.support.shop.ShopFragment

@LogTag("ShopActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class ShopActivity : BaseFontActivity() {

    lateinit var supportFragment: SupportFragment

    override fun setLayoutResourceId(): Int {
        return R.layout.support_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.let {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // for drawing behind status bar
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

            //make system bar to be translucent
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

            //make status bar color transparent
            window.statusBarColor = Color.TRANSPARENT

            var flags = it.decorView.systemUiVisibility
            // make dark status bar icons
            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            window.decorView.systemUiVisibility = flags
        }

        supportFragment =
            supportFragmentManager.findFragmentById(R.id.support_fragment) as SupportFragment

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    ShopFragment(), "shop"
                )
                .commit()
        }
    }
}