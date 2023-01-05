package vn.loitp.a.u

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.utilities.*
import com.loitp.core.utilities.LUIUtil.Companion.scrollToBottom
import com.loitp.core.utilities.statusbar.StatusBarCompat
import kotlinx.android.synthetic.main.a_utils_core.*
import vn.loitp.R
import java.math.BigDecimal

@LogTag("UtilsCoreActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class UtilsCoreActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_utils_core
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(
                view = this.ivIconLeft,
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = UtilsCoreActivityFont::class.java.simpleName
        }
        btSetStatusBarColorAlpha.setSafeOnClickListener {
            StatusBarCompat.setStatusBarColor(this, Color.RED, 50)
        }
        btSetStatusBarColor.setSafeOnClickListener {
            StatusBarCompat.setStatusBarColor(this, Color.RED)
        }
        btTranslucentStatusBar.setSafeOnClickListener {
            StatusBarCompat.translucentStatusBar(this, true)
        }
        btHideSystemUI.setSafeOnClickListener {
            LActivityUtil.hideSystemUI(layoutRootView)
        }
        btShowSystemUI.setSafeOnClickListener {
            LActivityUtil.showSystemUI(layoutRootView)
        }
        btPlayRotate.setSafeOnClickListener {
            LAnimationUtil.playRotate(it, null)
        }
        btSlideInDown.setSafeOnClickListener {
            LAnimationUtil.slideInDown(it)
        }
        btSlideInUp.setSafeOnClickListener {
            LAnimationUtil.slideInUp(it)
        }
        btPlayAnimRandomDuration.setSafeOnClickListener {
            LAnimationUtil.playAnimRandomDuration(it)
        }
        btConvertNumberToStringFormat.setSafeOnClickListener {
            showShortInformation(LConvertUtil.convertNumberToStringFormat(System.currentTimeMillis()))
        }
        btConvertNumberToString.setSafeOnClickListener {
            showShortInformation(LConvertUtil.convertNumberToString(System.currentTimeMillis()))
        }
        btConvertNumberToPercent.setSafeOnClickListener {
            showShortInformation(LConvertUtil.convertNumberToPercent(System.currentTimeMillis()))
        }
        btRoundBigDecimal.setSafeOnClickListener {
            showShortInformation(
                "roundBigDecimal: ${
                    LConvertUtil.roundBigDecimal(
                        BigDecimal(
                            60.123456
                        ), 3
                    )
                }"
            )
        }
        btLDateUtil.setSafeOnClickListener {
            val msg =
                "currentDate: ${LDateUtil.currentDate}" +
                        "\ncurrentYearMonth: ${LDateUtil.currentYearMonth}" +
                        "\ncurrentMonth: ${LDateUtil.currentMonth}" +
                        "\nconvertFormatDate: ${
                            LDateUtil.convertFormatDate(
                                "12/03/2022 01:02:03",
                                "dd/MM/yyyy Hh:mm:ss",
                                "yyyy/MM/dd"
                            )
                        }" +
                        "\nstringToDate: ${
                            LDateUtil.stringToDate(
                                "12/03/2022 01:02:03",
                                "dd/MM/yyyy Hh:mm:ss"
                            )
                        }" +
                        "\ndateToString: ${
                            LDateUtil.dateToString(
                                LDateUtil.getDate(year = 1993, month = 2, day = 4), "yyyy/MM/dd"
                            )
                        }" +
                        "\nformatDatePicker: ${
                            LDateUtil.formatDatePicker(
                                year = 1993, month = 2, day = 4, format = "yyyy/MM/dd"
                            )
                        }" +
                        "\ngetDateWithoutTime: ${LDateUtil.getDateWithoutTime("04/02/1993")}" +
                        "\nconvertDateToTimestamp: ${LDateUtil.convertDateToTimestamp("14-09-2017")}" +
                        "\nzeroTime: ${
                            LDateUtil.zeroTime(
                                LDateUtil.getDate(
                                    year = 1993,
                                    month = 2,
                                    day = 4
                                )
                            )
                        }" +
                        "\nconvertDateToTimeStamp: ${LDateUtil.convertDateToTimeStamp("1993-02-04'T'11:22:33")}" +
                        "\nconvertStringToCalendar: ${LDateUtil.convertStringToCalendar("1993-02-04")}" +
                        "\nconvertStringDate: ${
                            LDateUtil.convertStringDate(
                                "02/04/1993",
                                "MM/dd/yyyy"
                            )
                        }"
            showDialogMsg(msg)
        }
        btLDeviceUtil.setSafeOnClickListener {
            showDialogMsg(
                "isNavigationBarAvailable:${LDeviceUtil.isNavigationBarAvailable}" +
                        "\nisTablet: ${LDeviceUtil.isTablet()}" +
                        "\nisCanOverlay: ${LDeviceUtil.isCanOverlay()}" +
                        "\nisEmulator: ${LDeviceUtil.isEmulator()}" +
                        "\ngetDeviceId: ${LDeviceUtil.getDeviceId(this)}"
            )
        }
        btSetClipboard.setSafeOnClickListener {
            showShortInformation(LDeviceUtil.setClipboard("setClipboard ${System.currentTimeMillis()}"))
        }
        btLDisplayUtil.setSafeOnClickListener {
            showDialogMsg(
                "px2dip: ${LDisplayUtil.px2dip(6.9f)}" +
                        "\npx2sp: ${LDisplayUtil.px2sp(6.9f)}" +
                        "\nsp2px: ${LDisplayUtil.sp2px(6.9f)}" +
                        "\ngetDialogW: ${LDisplayUtil.getDialogW(this)}" +
                        "\ngetScreenW: ${LDisplayUtil.getScreenW(this)}" +
                        "\ngetScreenH: ${LDisplayUtil.getScreenH(this)}"
            )
        }
        btToggleKeyboard.setSafeOnClickListener {
            LDisplayUtil.toggleKeyboard()
        }
        btLImageUtil.setSafeOnClickListener {
            showDialogMsg("randomUrlFlickr: ${LImageUtil.randomUrlFlickr}")
        }
        btLMathUtil.setSafeOnClickListener {
            showDialogMsg(
                "roundBigDecimal ${
                    LMathUtil.roundBigDecimal(
                        value = BigDecimal(6.123456),
                        newScale = 3
                    )
                }" +
                        "\ngetUSCLN: ${LMathUtil.getUSCLN(6, 9)}" +
                        "\ngetBSCNN: ${LMathUtil.getBSCNN(6, 9)}"
            )
        }
        btShowStatusBar.setSafeOnClickListener {
            LScreenUtil.showStatusBar(this)
        }
        btHideStatusBar.setSafeOnClickListener {
            LScreenUtil.hideStatusBar(this)
        }
        btHideNavigationBar.setSafeOnClickListener {
            LScreenUtil.hideNavigationBar(this)
        }
        btShowNavigationBar.setSafeOnClickListener {
            LScreenUtil.showNavigationBar(this)
        }
        btHideDefaultControls.setSafeOnClickListener {
            LScreenUtil.hideDefaultControls(this)
        }
        btShowDefaultControls.setSafeOnClickListener {
            LScreenUtil.showDefaultControls(this)
        }
        btSetBrightness.setSafeOnClickListener {
            LScreenUtil.setBrightness(this, 99)
            showShortInformation("getCurrentBrightness: ${LScreenUtil.getCurrentBrightness()}")
        }
        btSendSMS.setSafeOnClickListener {
            LSMSUtil.sendSMS(this, "sendSMS from Loitp ${System.currentTimeMillis()}")
        }
        btLStoreUtil.setSafeOnClickListener {
            onClickBtLStoreUtil()
        }
        btLStringUtil.setSafeOnClickListener {
            onClickBtLStringUtil()
        }
        btLValidateUtil.setSafeOnClickListener {
            onClickBtLValidateUtil()
        }

        LUIUtil.setMarquee(tv, getString(R.string.large_dummy_text))
        //TODO fix setBackgroundDrawable
        v1.setBackgroundDrawable(LUIUtil.createGradientDrawableWithRandomColor())
        v2.setBackgroundDrawable(LUIUtil.createGradientDrawableWithColor(Color.RED, Color.GREEN))
        LUIUtil.setCircleViewWithColor(v3, Color.GREEN, Color.CYAN)
        LUIUtil.setGradientBackground(v4)
        LUIUtil.setTextFromHTML(
            tv1, "<p>This is Loitp<br />\n" +
                    "Dep Trai<br />\n" +
                    "Vip<br />\n" +
                    "Pro No1</p>"
        )
        LUIUtil.setTextBold(tv1)
        LUIUtil.setImageFromAsset("ic_gift.png", iv)
        LUIUtil.setProgressViewOffset(srl, 200)
        LUIUtil.setImeiActionSearch(et1) { }
        LUIUtil.setColorSeekBar(sb1, Color.RED)
        LUIUtil.setMarginsDp(sb1, 16, 16, 16, 16)
        LUIUtil.setRandomBackground(layoutRootView)
        LUIUtil.getAllChildren(layoutRootView).forEach {
            logD(">>>getAllChildren ${it.id}")
        }
        LUIUtil.setCheckBoxColor(cb1, Color.GREEN, Color.CYAN)
        LUIUtil.setChangeStatusBarTintToDark(window, false)
        btScrollToBottom.setSafeOnClickListener {
            sv.scrollToBottom()
        }
        LUIUtil.setDrawableTintColor(tv1, Color.CYAN)
    }

    private fun onClickBtLStoreUtil() {
        showShortInformation("Check logcat")
        logD("isSdPresent ${LStoreUtil.isSdPresent}")
        logD("randomColorLight ${LStoreUtil.randomColorLight}")
        val file = LStoreUtil.writeToFile(folder = "test", fileName = "test1.txt", body = "loitp")
        logD("writeToFile file ${file?.path}")
        val text = LStoreUtil.readTxtFromFolder(folderName = "test", fileName = "test1.txt")
        logD("readTxtFromFolder text $text")
        val textAsset = LStoreUtil.readTxtFromAsset("txt.txt")
        logD("textAsset $textAsset")
        logD("getAvailableSpaceInMb ${LStoreUtil.getAvailableSpaceInMb()}")
        logD("getAvailableRAM ${LStoreUtil.getAvailableRAM()}")
    }

    private fun onClickBtLStringUtil() {
        showShortInformation("Check logcat")
        val s = LStringUtil.convertHTMLTextToPlainText(
            "<p>This is Loitp<br />\n" +
                    "Dep Trai<br />\n" +
                    "Vip<br />\n" +
                    "Pro No1</p>"
        )
        logD("s $s")
    }

    private fun onClickBtLValidateUtil() {
        showShortInformation("Check logcat")
        logD("isValidEmail freuss47: ${LValidateUtil.isValidEmail("freuss47")}")
        logD("isValidEmail freuss47@gmail.com: ${LValidateUtil.isValidEmail("freuss47@gmail.com")}")
        logD("isValidPassword 123456: ${LValidateUtil.isValidPassword("123456")}")
        logD("isValidPassword 123456abcd: ${LValidateUtil.isValidPassword("123456abcd")}")
        logD("isValidPassword 123456abcdA@: ${LValidateUtil.isValidPassword("123456abcdA@")}")
    }
}