package vn.loitp.a.demo.architectureComponent.vm

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.getDateCurrentTimeZoneMls
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_demo_view_model.*
import vn.loitp.R
import vn.loitp.up.a.pattern.mvp.User
import java.util.*

// https://codinginfinite.com/architecture-component-viewmodel-example/

@LogTag("ViewModelActivity")
@IsFullScreen(false)
class ViewModelActivityFont : BaseActivityFont() {

    private lateinit var colorChangerViewModel: ColorChangerViewModel
    private lateinit var timeChangerViewModel: TimeChangerViewModel

    override fun setLayoutResourceId(): Int {
        return R.layout.a_demo_view_model
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = ViewModelActivityFont::class.java.simpleName
        }

        colorChangerViewModel = ViewModelProvider(this)[ColorChangerViewModel::class.java]
        colorChangerViewModel.colorResource.observe(
            this
        ) {
            ll.setBackgroundColor(it)
        }
        btChangeColor.setOnClickListener {
            colorChangerViewModel.colorResource.value = generateRandomColor()
        }

        val defUser = User()
        defUser.fullName = "Loitp"
        defUser.email = "www.muathu@gmail.com"
        btChangeUser.setOnClickListener {
            val user = User()
            user.fullName = "Loitp" + System.currentTimeMillis()
            user.email = "www.muathu@gmail.com" + System.currentTimeMillis()
        }

        timeChangerViewModel = ViewModelProvider(this)[TimeChangerViewModel::class.java]
        var countToStop = 0
        timeChangerViewModel.timerValue.observe(
            this,
            Observer {
                countToStop++
                logD("countToStop $countToStop")
                if (countToStop >= 15) {
                    logD("countToStop $countToStop -> STOP")
                    tvTime.text = "countToStop: $countToStop -> STOP"
                    timeChangerViewModel.timerValue.removeObservers(this)
                    return@Observer
                }
                tvTime.text =
                    "countToStop: $countToStop -> $it -> " + it.getDateCurrentTimeZoneMls(
                        "yyyy-MM-dd HH:mm:ss"
                    )
            }
        )
    }

    private fun generateRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
