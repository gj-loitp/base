package com.loitp.core.helper.fbComment

import android.annotation.SuppressLint
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import com.loitp.R
import com.loitp.annotation.IsSwipeActivity
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.FACEBOOK_COMMENT_URL
import com.loitp.core.ext.isDebugMode
import com.loitp.core.ext.setColorProgressBar
import com.loitp.core.ext.setDelay
import com.loitp.core.ext.setProgressBarVisibility
import com.loitp.databinding.LAFbCmtCoreBinding

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("FbCommentActivity")
@IsSwipeActivity(true)
class FbCommentActivity : BaseActivityFont() {
    internal var isLoading: Boolean = false
    private var postUrl: String? = null
    private var mWebViewPop: WebView? = null
    private lateinit var binding: LAFbCmtCoreBinding

    companion object {
        // the default number of comments should be visible
        // on page load.
        private const val NUMBER_OF_COMMENTS = 50
    }

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LAFbCmtCoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setLoading(isLoading = true)
        loadComments()
    }

    private fun setupViews() {
        setupActionBar()

        binding.progressBar.setColorProgressBar(
            color = getColor(R.color.colorPrimary)
        )

        postUrl = if (isDebugMode()) {
            "https://www.androidhive.info/2016/06/android-firebase-integrate-analytics/"
        } else {
            intent.getStringExtra(FACEBOOK_COMMENT_URL)
        }

        // finish the activity in case of empty url
        if (TextUtils.isEmpty(postUrl)) {
            showShortError("The web url shouldn't be empty")
            onBaseBackPressed()
            return
        }
    }

    private fun setupActionBar() {
//        binding.lActionBar.apply {
//            this.ivIconLeft?.setSafeOnClickListener {
//                onBaseBackPressed()
//            }
//            this.ivIconRight?.isVisible = false
//            this.tvTitle?.text = getString(R.string.fb_comment)
//        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadComments() {
        binding.commentsWebView.apply {
            webViewClient = UriWebViewClient()
            webChromeClient = UriChromeClient()
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.setSupportZoom(false)
            settings.builtInZoomControls = false
            CookieManager.getInstance().setAcceptCookie(true)
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            // facebook comment widget including the article url
            val html =
                "<!doctype html> <html lang=\"en\"> <head></head> <body> " + "<div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.6\"; fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script> " + "<div class=\"fb-comments\" data-href=\"" + postUrl + "\" " + "data-numposts=\"" + NUMBER_OF_COMMENTS + "\" data-order-by=\"reverse_time\">" + "</div> </body> </html>"

            loadDataWithBaseURL("http://www.nothing.com", html, "text/html", "UTF-8", null)
            minimumHeight = 200
        }
    }

    private fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        if (isLoading) {
            binding.progressBar.setProgressBarVisibility(View.VISIBLE)
        } else {
            setDelay(mls = 1000, runnable = {
                binding.progressBar.setProgressBarVisibility(
                    visibility = View.GONE
                )
            })
        }
        invalidateOptionsMenu()
    }

    private inner class UriWebViewClient : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val host = Uri.parse(url).host
            return host != "m.facebook.com"
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            // val host = Uri.parse(url).host
            setLoading(false)
            if (url.contains(other = "/plugins/close_popup.php?reload")) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    binding.rlWebview.removeView(mWebViewPop)
                    loadComments()
                }, 600)
            }
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            setLoading(false)
        }
    }

    private inner class UriChromeClient : WebChromeClient() {

        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateWindow(
            view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message
        ): Boolean {
            mWebViewPop = WebView(applicationContext)
            mWebViewPop?.let {
                it.isVerticalScrollBarEnabled = false
                it.isHorizontalScrollBarEnabled = false
                it.webViewClient = UriWebViewClient()
                it.webChromeClient = this
                it.settings.javaScriptEnabled = true
                it.settings.domStorageEnabled = true
                it.settings.setSupportZoom(false)
                it.settings.builtInZoomControls = false
                it.settings.setSupportMultipleWindows(true)
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                binding.rlWebview.addView(it)
            }
            val transport = resultMsg.obj as WebView.WebViewTransport
            transport.webView = mWebViewPop
            resultMsg.sendToTarget()
            return true
        }

        override fun onConsoleMessage(cm: ConsoleMessage): Boolean {
            logD("onConsoleMessage: " + cm.message())
            return true
        }

        override fun onCloseWindow(window: WebView) {}
    }
}
