package vn.loitp.app.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import com.core.base.BaseFontActivity
import com.core.utilities.*
import com.interfaces.GGSettingCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.model.App
import com.views.LToast
import okhttp3.*
import vn.loitp.app.BuildConfig
import vn.loitp.app.R
import vn.loitp.app.app.LApplication
import java.io.IOException

class SplashActivity : BaseFontActivity() {
    private var isAnimDone = false
    private var isCheckReadyDone = false
    private var isShowDialogCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LUIUtil.setDelay(mls = 2500, runnable = Runnable {
            isAnimDone = true
            goToHome()
        })
        val tv = findViewById<TextView>(R.id.tv)
        tv.text = "Version ${BuildConfig.VERSION_NAME}"

        val tvPolicy = findViewById<TextView>(R.id.tv_policy)
        LUIUtil.setTextShadow(tvPolicy)
        tvPolicy.setOnClickListener { LSocialUtil.openBrowserPolicy(activity) }

        getSettingFromGGDrive()

        startIdleTimeHandler(10 * 1000)
        //val getAddressLog = DebugDB.getAddressLog()
        //LLog.d(TAG, "getAddressLog $getAddressLog")
    }

    override fun onActivityUserIdleAfterTime(delayMlsIdleTime: Long, isIdleTime: Boolean) {
        super.onActivityUserIdleAfterTime(delayMlsIdleTime, isIdleTime)
        LToast.showLong(activity, "onActivityUserIdleAfterTime delayMlsIdleTime $delayMlsIdleTime, isIdleTime: $isIdleTime")
    }

    override fun onResume() {
        super.onResume()
        if (!isShowDialogCheck) {
            checkPermission()
        }
    }

    private fun checkPermission() {
        isShowDialogCheck = true
        val isCanWriteSystem = LScreenUtil.checkSystemWritePermission(activity)
        if (isCanWriteSystem) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                val isNeedCheckReady = false
                                if (isNeedCheckReady) {
                                    checkReady()
                                } else {
                                    isCheckReadyDone = true
                                    goToHome()
                                }
                            } else {
                                showShouldAcceptPermission()
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied) {
                                showSettingsDialog()
                            }
                            isShowDialogCheck = true
                        }

                        override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    })
                    .onSameThread()
                    .check()
        } else {
            val alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to allow modifying system settings", "Okay", "Exit", object : LDialogUtil.Callback2 {
                override fun onClick1() {
                    isShowDialogCheck = false
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    intent.data = Uri.parse("package:" + activity.packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                    LActivityUtil.tranIn(activity)
                }

                override fun onClick2() {
                    onBackPressed()
                }
            })
            alertDialog.setCancelable(false)
        }
    }

    private fun goToHome() {
        //String s = LStoreUtil.readTxtFromAsset(activity, "news.json");
        //LLog.d(TAG, "goToHome " + s);
        if (isAnimDone && isCheckReadyDone) {
            val intent = Intent(activity, MenuActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(activity)
            LUIUtil.setDelay(1000, Runnable {
                finish()
            })
        }
    }

    private fun showDialogNotReady() {
        runOnUiThread {
            val title = if (LConnectivityUtil.isConnected(activity)) {
                "This app is not available now"
            } else {
                getString(R.string.check_ur_connection)
            }
            val alertDial = LDialogUtil.showDialog1(activity, "Warning", title, "Ok",
                    object : LDialogUtil.Callback1 {
                        override fun onClick1() {
                            onBackPressed()
                        }
                    })
            alertDial.setCancelable(false)
        }
    }

    private fun checkReady() {
        if (LPrefUtil.getCheckAppReady(activity)) {
            isCheckReadyDone = true
            goToHome()
            return
        }
        val LINK_GG_DRIVE_CHECK_READY = "https://drive.google.com/uc?export=download&id=1LHnBs4LG1EORS3FtdXpTVwQW2xONvtHo"
        val request = Request.Builder().url(LINK_GG_DRIVE_CHECK_READY).build()
        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logE("onFailure $e")
                showDialogNotReady()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    //LLog.d(TAG, "onResponse isSuccessful " + response.toString());
                    val versionServer = Integer.parseInt(response.body()!!.string())
                    logD("onResponse $versionServer")
                    if (versionServer == 1) {
                        isCheckReadyDone = true
                        LPrefUtil.setCheckAppReady(activity, true)
                        goToHome()
                    } else {
                        showDialogNotReady()
                    }
                } else {
                    logD("onResponse !isSuccessful: $response")
                    showDialogNotReady()
                }
            }
        })
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String {
        return javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    private fun showSettingsDialog() {
        val alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to use this feature. You can grant them in app settings.", "GOTO SETTINGS", "Cancel", object : LDialogUtil.Callback2 {
            override fun onClick1() {
                isShowDialogCheck = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, 101)
            }

            override fun onClick2() {
                onBackPressed()
            }
        })
        alertDialog.setCancelable(false)
    }

    private fun showShouldAcceptPermission() {
        val alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to use this feature.", "Okay", "Cancel", object : LDialogUtil.Callback2 {
            override fun onClick1() {
                checkPermission()
            }

            override fun onClick2() {
                onBackPressed()
            }
        })
        alertDialog.setCancelable(false)
    }

    private fun getSettingFromGGDrive() {
        val linkGGDriveConfigSetting = "https://drive.google.com/uc?export=download&id=1xqNJBQMzCPzAiAcm673B6ErRRRANCmQT"
        LStoreUtil.getSettingFromGGDrive(activity, linkGGDriveConfigSetting, object : GGSettingCallback {
            override fun onGGFailure(call: Call, e: IOException) {
            }

            override fun onGGResponse(app: App?, isNeedToShowMsg: Boolean) {
                logD("getSettingFromGGDrive setting " + isNeedToShowMsg + " -> " + LApplication.gson.toJson(app))
            }
        })
    }
}
