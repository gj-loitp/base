package vn.loitp.app.a.demo.floatingVideo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseFontActivity
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.utilities.LDialogUtil
import com.loitp.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_demo_floating_video.*
import vn.loitp.R

@LogTag("FloatingWidgetActivity")
@IsFullScreen(false)
class FloatingWidgetActivity : BaseFontActivity() {

    companion object {
        private const val CODE_DRAW_OVER_OTHER_APP_PERMISSION_SHOW = 2084
        private const val CODE_DRAW_OVER_OTHER_APP_PERMISSION_EDGE = 2085
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_demo_floating_video
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
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = FloatingWidgetActivity::class.java.simpleName
        }
        btShow.setSafeOnClickListener {
            handleShow()
        }
        btEdge.setSafeOnClickListener {
            handleEdge()
        }
    }

    private fun handleShow() {
        LDialogUtil.showDialog2(
            context = this,
            title = "Permission",
            msg = "Plz open overlay permisson",
            button1 = "Yes",
            button2 = "No",
            onClickButton1 = {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                //TODO startActivityForResult
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION_SHOW)
            },
            onClickButton2 = {
            }
        )
    }

    private fun handleEdge() {
        LDialogUtil.showDialog2(
            context = this,
            title = "Permission",
            msg = "Plz open overlay permisson",
            button1 = "Yes",
            button2 = "No",
            onClickButton1 = {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                //TODO startActivityForResult
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION_EDGE)
            },
            onClickButton2 = {
                //do nothing
            }
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION_SHOW) {
            handleShow()
        } else if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION_EDGE) {
            handleEdge()
        }
    }
}