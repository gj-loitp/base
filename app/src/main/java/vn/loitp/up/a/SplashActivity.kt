package vn.loitp.up.a

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.*
import com.permissionx.guolindev.PermissionX
import vn.loitp.BuildConfig
import vn.loitp.R
import vn.loitp.databinding.ASplashBinding
import vn.loitp.up.a.anim.konfetti.Presets

@SuppressLint("CustomSplashScreen")
@LogTag("SplashActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class SplashActivity : BaseActivityFont() {
    private lateinit var binding: ASplashBinding

    private var isAnimDone = false
//    private var isCheckReadyDone = false
    private var isShowDialogCheck = false

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ASplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        binding.konfettiView.start(Presets.festive())
        setDelay(mls = 2500, runnable = {
            isAnimDone = true
            goToHome()
        })
        binding.textViewVersion.text = "Version ${BuildConfig.VERSION_NAME}"

        binding.tvPolicy.apply {
            this.setTextUnderline()
            this.setTextShadow(color = null)
            setOnClickListener {
                this@SplashActivity.openBrowserPolicy()
            }
        }

        startIdleTimeHandler(10 * 1000)
        // val getAddressLog = DebugDB.getAddressLog()
    }

    override fun onActivityUserIdleAfterTime(
        delayMlsIdleTime: Long, isIdleTime: Boolean
    ) {
        super.onActivityUserIdleAfterTime(delayMlsIdleTime, isIdleTime)
        showShortInformation("onActivityUserIdleAfterTime delayMlsIdleTime $delayMlsIdleTime, isIdleTime: $isIdleTime")
    }

    override fun onResume() {
        super.onResume()
        if (!isShowDialogCheck) {
            checkPermission()
        }
    }

    private fun checkPermission() {
        isShowDialogCheck = true
        val color = if (isDarkTheme()) {
            Color.WHITE
        } else {
            Color.BLACK
        }

        val listPer = ArrayList<String>()
        listPer.add(Manifest.permission.ACCESS_FINE_LOCATION)
        listPer.add(Manifest.permission.CAMERA)

//        listPer.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //quyen nhay cam, can khai bao voi google
            listPer.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listPer.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        //ACCESS_BACKGROUND_LOCATION publish len store rat kho khan, khong can thiet
        //ban build debug thi chi test de biet feat nay work
        //con ban release thi khong can dau
        //nho uncomment per ACCESS_BACKGROUND_LOCATION trong manifest
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && BuildConfig.DEBUG) {
//                listPer.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//            }

        PermissionX.init(this).permissions(listPer).setDialogTintColor(color, color)
            .onExplainRequestReason { scope, deniedList, _ ->
                val message = getString(R.string.app_name) + getString(com.loitp.R.string.needs_per)
                scope.showRequestReasonDialog(
                    permissions = deniedList,
                    message = message,
                    positiveText = getString(com.loitp.R.string.allow),
                    negativeText = getString(com.loitp.R.string.deny)
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    permissions = deniedList,
                    message = getString(com.loitp.R.string.per_manually_msg),
                    positiveText = getString(com.loitp.R.string.ok),
                    negativeText = getString(R.string.cancel)
                )
            }.request { allGranted, _, _ ->
                if (allGranted) {
//                    val isNeedCheckReady = false
//                    if (isNeedCheckReady) {
//                        checkReady()
//                    } else {
//                        isCheckReadyDone = true
//                        goToHome()
//                    }
//                    isCheckReadyDone = true
                    goToHome()
                } else {
                    finish()//correct
                    this.tranOut()
                }
                isShowDialogCheck = false
            }
    }

    private fun isCanWriteSystem(
        onSuccess: Runnable
    ) {
        val isCanWriteSystem = checkSystemWritePermission()
        if (isCanWriteSystem) {
            onSuccess.run()
        } else {
            val alertDialog = this.showDialog2(title = "Need Permissions",
                msg = "This app needs permission to allow modifying system settings",
                button1 = getString(com.loitp.R.string.ok),
                button2 = getString(R.string.cancel),
                onClickButton1 = {
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:$packageName")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this@SplashActivity.tranIn()
                },
                onClickButton2 = {
                    onBaseBackPressed()
                })
            alertDialog.setCancelable(false)
        }
    }

    private fun goToHome() {
//        logD("goToHome isAnimDone $isAnimDone, isCheckReadyDone $isCheckReadyDone")
//        if (isAnimDone && isCheckReadyDone) {
        if (isAnimDone) {
            runOnUiThread {
                isCanWriteSystem(onSuccess = {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    this.tranIn()
                    finish()
                })
            }
        }
    }

//    private fun showDialogNotReady() {
//        runOnUiThread {
//            val title = if (this.isConnected()) {
//                "This app is not available now"
//            } else {
//                getString(R.string.check_ur_connection)
//            }
//            val alertDial =
//                this.showDialog1(
//                    title = "Warning",
//                    msg = title,
//                    button1 = "Ok",
//                    onClickButton1 = {
//                        onBaseBackPressed()
//                    },
//                    onLongClickButton1 = {
//                        //long click to by pass, this feature is hidden and normal user dont know that
//                        isCheckReadyDone = true
//                        goToHome()
//                    }
//                )
//            alertDial.setCancelable(false)
//        }
//    }

//    private fun checkReady() {
//        if (getCheckAppReady()) {
//            val app = getGGAppSetting()
//            val isFullData = app?.config?.isFullData == true
//            if (isFullData) {
//                setCheckAppReady(true)
//                isCheckReadyDone = true
//                goToHome()
//                return
//            } else {
//                //continue to download config from drive
//            }
//        }
//        //https://drive.google.com/drive/u/0/folders/1STvbrMp_WSvPrpdm8DYzgekdlwXKsCS9
//        val linkGGDriveConfigSetting =
//            "https://drive.google.com/uc?export=download&id=16pwq28ZTeP5p1ZeJmgwjHsOofE12XRIf"
//        getSettingFromGGDrive(linkGGDriveSetting = linkGGDriveConfigSetting,
//            onGGFailure = { _: Call, _: IOException ->
//                showDialogNotReady()
//            },
//            onGGResponse = { app: App? ->
//                logD(">>>checkReady " + BaseApplication.gson.toJson(app))
//                if (app == null || app.config?.isReady == false) {
//                    showDialogNotReady()
//                } else {
//                    setCheckAppReady(true)
//                    setGGAppSetting(app)
//                    isCheckReadyDone = true
//                    goToHome()
//                }
//            })
//    }
}
