package vn.loitp.app.activity.customviews.switchtoggle.togglebuttongroup

import android.content.Intent
import android.os.Bundle
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LActivityUtil
import com.loitpcore.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_switch_tbg_menu.*
import vn.loitp.app.R

// https://github.com/nex3z/ToggleButtonGroup
@LogTag("TBGMenuActivity")
@IsFullScreen(false)
class TBGMenuActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_switch_tbg_menu
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        btnMultiSelectSample.setSafeOnClickListener {
            val intent = Intent(this, TBGMultiSelectActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
        }
        btnSingleSelectSample.setSafeOnClickListener {
            val intent = Intent(this, TBGSingleSelectActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
        }
        btnLabelSample.setSafeOnClickListener {
            val intent = Intent(this, TBGFlowLabelActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
        }
        btnCustomButtonSample.setSafeOnClickListener {
            val intent = Intent(this, TBGCustomButtonActivity::class.java)
            startActivity(intent)
            LActivityUtil.tranIn(this)
        }
    }
}
