package vn.loitp.up.a.cv.bs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseBottomSheetFragment
import com.loitp.core.common.AD_HELPER_IS_ENGLISH_LANGUAGE
import com.loitp.core.ext.*
import com.loitp.core.helper.adHelper.AdHelperActivity
import vn.loitp.R

/**
 * Created by Loitp on 04,August,2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("SampleBaseBottomSheet")
class SampleBaseBottomSheet :
    BaseBottomSheetFragment(
        layoutId = R.layout.l_bottom_sheet_sample,
        height = WindowManager.LayoutParams.WRAP_CONTENT,
        isDraggable = true,
        firstBehaviourState = BottomSheetBehavior.STATE_EXPANDED
    ),
    View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private var btRateApp: View? = null
    private var btMoreApp: View? = null
    private var btShareApp: View? = null
    private var btLikeFbFanpage: View? = null
    private var btSupport: View? = null
    private var btAdHelper: View? = null
    private var btShowDialogProgress: View? = null
    private var btHideDialogProgress: View? = null

    private fun setupViews() {
        btRateApp = view?.findViewById(R.id.btRateApp)
        btMoreApp = view?.findViewById(R.id.btMoreApp)
        btShareApp = view?.findViewById(R.id.btShareApp)
        btLikeFbFanpage = view?.findViewById(R.id.btLikeFbFanpage)
        btSupport = view?.findViewById(R.id.btSupport)
        btAdHelper = view?.findViewById(R.id.btAdHelper)
        btShowDialogProgress = view?.findViewById(R.id.btShowDialogProgress)
        btHideDialogProgress = view?.findViewById(R.id.btHideDialogProgress)

        btRateApp?.setOnClickListener(this)
        btMoreApp?.setOnClickListener(this)
        btShareApp?.setOnClickListener(this)
        btLikeFbFanpage?.setOnClickListener(this)
        btSupport?.setOnClickListener(this)
        btAdHelper?.setOnClickListener(this)
        btShowDialogProgress?.setOnClickListener(this)
        btHideDialogProgress?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        activity?.let {
            when (v) {
                btRateApp -> it.rateApp(packageName = it.packageName)
                btMoreApp -> it.moreApp()
                btShareApp -> it.shareApp()
                btLikeFbFanpage -> it.likeFacebookFanpage()
                btSupport -> it.chatMessenger()
                btAdHelper -> {
                    val intent = Intent(it, AdHelperActivity::class.java)
                    intent.putExtra(AD_HELPER_IS_ENGLISH_LANGUAGE, false)
                    startActivity(intent)
                    it.tranIn()
                }
                btShowDialogProgress -> {
                    showDialogProgress()
                    setDelay(2000) {
                        hideDialogProgress()
                    }
                }
                btHideDialogProgress -> {
                    hideDialogProgress()
                }
            }
        }
//        dismiss()
    }
}