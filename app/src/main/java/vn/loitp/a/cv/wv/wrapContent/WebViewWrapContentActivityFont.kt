package vn.loitp.a.cv.wv.wrapContent

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_wv_wrap_content.*
import vn.loitp.R

@LogTag("WebViewWrapContentActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class WebViewWrapContentActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_wv_wrap_content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.tvTitle?.text = WebViewWrapContentActivityFont::class.java.simpleName
        }


        wv.setBackgroundColor(Color.RED)
//        val content = HtmlContent.body
        val content = HtmlContent.body.replace("height:", "height_:")
        wv.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)

        wv.settings.javaScriptEnabled = true
//        wv.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//            }
//        }

        wv.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            @Suppress("unused")
            fun performClick(string: String?) {
                showLongWarning("click button close")
            }
        }, "onClickClose")

        wv.addJavascriptInterface(object : Any() {
            @JavascriptInterface
            @Suppress("unused")
            fun performClick(string: String?) {
                showLongWarning("click body")
            }
        }, "onClickBody")
    }
}