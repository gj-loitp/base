package com.loitp.core.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.loitp.R
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.IsKeepScreenOn
import com.loitp.annotation.IsShowAnimWhenExit
import com.loitp.annotation.IsSwipeActivity
import com.loitp.annotation.LogTag
import com.loitp.core.ext.allowInfiniteLines
import com.loitp.core.ext.d
import com.loitp.core.ext.e
import com.loitp.core.ext.genCustomProgressDialog
import com.loitp.core.ext.hide
import com.loitp.core.ext.i
import com.loitp.core.ext.isDarkTheme
import com.loitp.core.ext.recolorNavigationBar
import com.loitp.core.ext.recolorStatusBar
import com.loitp.core.ext.show
import com.loitp.core.ext.showDialog1
import com.loitp.core.ext.tranIn
import com.loitp.core.ext.tranOut
import com.loitp.core.ext.withBackground
import com.loitp.data.EventBusData
import com.loitp.views.bs.BottomSheetOptionFragment
import com.loitp.views.smoothTransition.SwitchAnimationUtil
import com.loitp.views.toast.LToast
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
// animation https://github.com/dkmeteor/SmoothTransition
abstract class BaseActivity : AppCompatActivity() {
    protected var compositeDisposable = CompositeDisposable()
    protected var logTag: String? = null
    protected var colorBkgProgressDialog = R.color.black65

    private var delayMlsIdleTime: Long = 60 * 1000 // 60s
    private var handlerIdleTime: Handler? = null
    private var runnableIdleTime: Runnable? = null
    private var isIdleTime = false
    private var isShowAnimWhenExit = true

    var alertDialogProgress: Dialog? = null

//    protected abstract fun setLayoutResourceId(): Int

    protected fun setTransparentStatusNavigationBar() {
        // https://stackoverflow.com/questions/29311078/android-completely-transparent-status-bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val tmpLogTag = javaClass.getAnnotation(LogTag::class.java)
        logTag = "logTag" + tmpLogTag?.value

        val isDarkTheme = isDarkTheme()
//        logD("onCreate isDarkTheme $isDarkTheme")
        val isSwipeActivity = javaClass.getAnnotation(IsSwipeActivity::class.java)?.value ?: false
//        logD("onCreate isSwipeActivity $isSwipeActivity")
        if (isSwipeActivity) {
            if (isDarkTheme) {
                setTheme(R.style.DarkSwipeTheme)
//                logD("onCreate setTheme DarkSwipeTheme")
            } else {
                setTheme(R.style.LightSwipeTheme)
//                logD("onCreate setTheme LightSwipeTheme")
            }
        } else {
            if (isDarkTheme) {
                setTheme(R.style.DarkTheme)
//                logD("onCreate setTheme DarkTheme")
            } else {
                setTheme(R.style.LightTheme)
//                logD("onCreate setTheme LightTheme")
            }
        }

//        setCustomStatusBar(
//            colorStatusBar = LAppResource.getColor(R.color.colorPrimary),
//            colorNavigationBar = LAppResource.getColor(R.color.colorPrimary)
//        )

        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        val isFullScreen = javaClass.getAnnotation(IsFullScreen::class.java)?.value ?: false
        if (isFullScreen) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // requestFeature() must be called before adding content
        }

//        val layoutId = setLayoutResourceId()
//        if (layoutId != NOT_FOUND) {
//            setContentView(setLayoutResourceId())
//        }

        if (isFullScreen) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                window.setDecorFitsSystemWindows(false)
//                window.insetsController?.let {
//                    it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                    it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                }
//            } else {
//                @Suppress("DEPRECATION")
//                window.setFlags(
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN
//                )
//            }

            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val isKeepScreenOn = javaClass.getAnnotation(IsKeepScreenOn::class.java)?.value ?: true
        if (isKeepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

//        registerNetworkChangeListener()

        // auto animation
        val isAutoAnimation = javaClass.getAnnotation(IsAutoAnimation::class.java)?.value ?: false
        if (isAutoAnimation) {
            SwitchAnimationUtil().startAnimation(
                root = window.decorView, type = SwitchAnimationUtil.AnimationType.ALPHA
//                type = SwitchAnimationUtil.AnimationType.SCALE
//                type = SwitchAnimationUtil.AnimationType.FLIP_VERTICAL
            )
        }
        isShowAnimWhenExit = javaClass.getAnnotation(IsShowAnimWhenExit::class.java)?.value ?: true

        onBackPressedDispatcher.addCallback(this) {
            onBaseBackPressed()
        }
    }

    open fun onBaseBackPressed() {
//        logE("onBaseBackPressed")
//        finish()//correct
        finishAfterTransition()
        if (isShowAnimWhenExit) {
            this.tranOut()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        stopIdleTimeHandler() // stop first and then start
        startIdleTimeHandler(delayMlsIdleTime)
    }

    open fun stopIdleTimeHandler() {
        handlerIdleTime?.let { h ->
            runnableIdleTime?.let { r ->
                h.removeCallbacks(r)
            }
        }
    }

    open fun onActivityUserIdleAfterTime(
        delayMlsIdleTime: Long, isIdleTime: Boolean
    ) {
        logD("onActivityUserIdleAfterTime delayMlsIdleTime: $delayMlsIdleTime, isIdleTime: $isIdleTime")
    }

    open fun startIdleTimeHandler(delayMls: Long) {
        delayMlsIdleTime = delayMls
        handlerIdleTime = Handler(Looper.getMainLooper())
        runnableIdleTime = Runnable {
            isIdleTime = true
            onActivityUserIdleAfterTime(delayMlsIdleTime = delayMlsIdleTime, isIdleTime = true)
        }
        handlerIdleTime?.let { h ->
            runnableIdleTime?.let { r ->
                isIdleTime = false
                h.postDelayed(r, delayMls)
            }
        }
    }

    fun setCustomStatusBar(
        colorStatusBar: Int, colorNavigationBar: Int
    ) {
        window.statusBarColor = colorStatusBar
        window.navigationBarColor = colorNavigationBar

        // set color for status bar
//        StatusBarCompat.setStatusBarColor(activity = this, statusColor = LAppResource.getColor(R.color.red))

        // add alpha to color
//        StatusBarCompat.setStatusBarColor(activity = this, statusColor = LAppResource.getColor(R.color.red), alpha = 50)

        // translucent status bar
//        StatusBarCompat.translucentStatusBar(activity = this)

        // should hide status bar background (default black background) when SDK >= 21
//        StatusBarCompat.translucentStatusBar(activity = this, hideStatusBarBackground = true)
    }

    fun changeStatusBarContrastStyle(
        lightIcons: Boolean,
        colorBackground: Int,
        withRecolorEfx: Boolean = true,
    ) {
        val decorView: View = this.window.decorView
        if (lightIcons) {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (withRecolorEfx) {
            this.recolorStatusBar(
                startColor = null,
                endColor = colorBackground,
                duration = 300
            )
            this.recolorNavigationBar(
                startColor = null,
                endColor = colorBackground,
                duration = 300
            )
        } else {
            this.setCustomStatusBar(
                colorStatusBar = colorBackground, colorNavigationBar = colorBackground
            )
        }
    }

    override fun onDestroy() {
//        unRegisterNetworkChangeListener()
        EventBus.getDefault().unregister(this)
        compositeDisposable.clear()
        stopIdleTimeHandler()
        super.onDestroy()
    }

    protected fun handleException(throwable: Throwable) {
        logE("handleException: $throwable")
        showDialogError("Error: $throwable")
    }

    protected fun showDialogError(
        errMsg: String?, runnable: Runnable? = null
    ) {
        if (errMsg.isNullOrEmpty()) {
            return
        }
        val alertDialog = this.showDialog1(
            title = getString(R.string.warning),
            msg = errMsg,
            button1 = getString(R.string.confirm),
            onClickButton1 = {
                runnable?.run()
            })
        alertDialog.setCancelable(false)
    }

    protected fun showDialogMsg(
        errMsg: String, runnable: Runnable? = null
    ) {
        this.showDialog1(
            title = getString(R.string.app_name),
            msg = errMsg,
            button1 = getString(R.string.confirm),
            onClickButton1 = {
                runnable?.run()
            })
    }

    fun showShortInformation(
        msg: String?,
        isTopAnchor:
        Boolean = true,
        drawableL: Int? = null,
        drawableT: Int? = null,
        drawableR: Int? = null,
        drawableB: Int? = null,
        padding: Int = 32,
    ) {
        LToast.showShortInformation(
            msg = msg,
            isTopAnchor = isTopAnchor,
            drawableL = drawableL,
            drawableT = drawableT,
            drawableR = drawableR,
            drawableB = drawableB,
            padding = padding,
        )
    }

    fun showShortWarning(
        msg: String?, isTopAnchor: Boolean = true
    ) {
        LToast.showShortWarning(msg = msg, isTopAnchor = isTopAnchor)
    }

    fun showShortError(
        msg: String?, isTopAnchor: Boolean = true
    ) {
        LToast.showShortError(msg = msg, isTopAnchor = isTopAnchor)
    }

    fun showLongInformation(
        msg: String?, isTopAnchor: Boolean = true
    ) {
        LToast.showLongInformation(msg = msg, isTopAnchor = isTopAnchor)
    }

    fun showLongWarning(
        msg: String?, isTopAnchor: Boolean = true
    ) {
        LToast.showLongWarning(msg = msg, isTopAnchor = isTopAnchor)
    }

    fun showLongError(
        msg: String?, isTopAnchor: Boolean = true
    ) {
        LToast.showLongError(msg = msg, isTopAnchor = isTopAnchor)
    }

    fun showShortDebug(msg: String?) {
        LToast.showShortDebug("Debug: $msg")
    }

    fun showLongDebug(msg: String?) {
        LToast.showLongDebug("Debug: $msg")
    }

    protected fun <T : ViewModel> getViewModel(className: Class<T>): T {
        return ViewModelProvider(this)[className]
    }

    protected fun logD(msg: String) {
        logTag?.let {
            d(it, msg)
        }
    }

    protected fun logE(msg: String) {
        logTag?.let {
            e(it, msg)
        }
    }

    protected fun logI(msg: String) {
        logTag?.let {
            i(it, msg)
        }
    }

    fun showBottomSheetOptionFragment(
        isCancelableFragment: Boolean = true,
        isShowIvClose: Boolean = true,
        title: String,
        message: String,
        textButton1: String? = null,
        textButton2: String? = null,
        textButton3: String? = null,
        onClickButton1: ((Unit) -> Unit)? = null,
        onClickButton2: ((Unit) -> Unit)? = null,
        onClickButton3: ((Unit) -> Unit)? = null,
        onDismiss: ((Unit) -> Unit)? = null
    ) {
        val bottomSheetOptionFragment = BottomSheetOptionFragment(
            isCancelableFragment = isCancelableFragment,
            isShowIvClose = isShowIvClose,
            title = title,
            message = message,
            textButton1 = textButton1,
            textButton2 = textButton2,
            textButton3 = textButton3,
            onClickButton1 = onClickButton1,
            onClickButton2 = onClickButton2,
            onClickButton3 = onClickButton3,
            onDismiss = onDismiss
        )
        bottomSheetOptionFragment.show(supportFragmentManager, bottomSheetOptionFragment.tag)
    }

    fun showSnackBarInfor(
        msg: String, view: View? = null, isFullWidth: Boolean = false
    ) {
        if (!this.isFinishing) {
            val anchorView = view ?: findViewById(android.R.id.content)
            val snackBar = Snackbar.make(anchorView, msg, Snackbar.LENGTH_LONG)
                .withBackground(R.drawable.bg_toast_infor).allowInfiniteLines()
            if (isFullWidth) {
                snackBar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            snackBar.show()
        }
    }

    fun showSnackBarWarning(
        msg: String, view: View? = null, isFullWidth: Boolean = false
    ) {
        if (!this.isFinishing) {
            val anchorView = view ?: findViewById(android.R.id.content)
            val snackBar = Snackbar.make(anchorView, msg, Snackbar.LENGTH_LONG)
                .withBackground(R.drawable.bg_toast_warning).allowInfiniteLines()
            if (isFullWidth) {
                snackBar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            snackBar.show()
        }
    }

    fun showSnackBarError(
        msg: String, view: View? = null, isFullWidth: Boolean = false
    ) {
        if (!this.isFinishing) {
            val anchorView = view ?: findViewById(android.R.id.content)
            val snackBar = Snackbar.make(anchorView, msg, Snackbar.LENGTH_LONG)
                .withBackground(R.drawable.bg_toast_err).allowInfiniteLines()
            if (isFullWidth) {
                snackBar.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            snackBar.show()
        }
    }

    fun showDialogProgress() {
        if (alertDialogProgress == null) {
            alertDialogProgress =
                this.genCustomProgressDialog(getColor(colorBkgProgressDialog))
        }
        alertDialogProgress.show()
    }

    fun hideDialogProgress() {
        alertDialogProgress.hide()
    }

    fun launchActivity(
        cls: Class<*>,
        withAnim: Boolean = true,
        data: ((Intent) -> Unit)? = null,
    ) {
        val intent = Intent(/* packageContext = */ this, /* cls = */ cls)
        data?.invoke(intent)
        startActivity(intent)
        if (withAnim) {
            this.tranIn()
        }
    }

    fun launchActivityForResult(
        cls: Class<*>,
        withAnim: Boolean = true,
        data: ((Intent) -> Unit)? = null,
    ) {
        val intent = Intent(/* packageContext = */ this, /* cls = */ cls)
        data?.invoke(intent)
        if (withAnim) {
            this.tranIn()
        }
    }

    fun launchActivityForResult(
        intent: Intent,
        withAnim: Boolean = true,
        data: ((Intent) -> Unit)? = null,
    ) {
        data?.invoke(intent)
        if (withAnim) {
            this.tranIn()
        }
    }

    fun setResultActivity(
        data: ((Intent) -> Unit)? = null
    ) {
        val i = Intent().apply {
            data?.invoke(this)
        }
        setResult(Activity.RESULT_OK, i)
        onBaseBackPressed()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventBusData.ConnectEvent) {
        onNetworkChange(event = event)
    }

    open fun onNetworkChange(event: EventBusData.ConnectEvent) {}
}
