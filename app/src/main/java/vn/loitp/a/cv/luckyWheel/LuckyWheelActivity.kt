package vn.loitp.a.cv.luckyWheel

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import com.bluehomestudio.luckywheel.WheelItem
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.openUrlInBrowser
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_lucky_wheel.*
import vn.loitp.R

@LogTag("LuckyWheelActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class LuckyWheelActivity : BaseActivityFont() {

    private var wheelItems = ArrayList<WheelItem>()

    override fun setLayoutResourceId(): Int {
        return R.layout.a_lucky_wheel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generateWheelItems()
        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.let {
                it.setSafeOnClickListenerElastic(runnable = {
                    context.openUrlInBrowser(
                        url = "https://github.com/mmoamenn/LuckyWheel_Android"
                    )
                })
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.tvTitle?.text = LuckyWheelActivity::class.java.simpleName
        }

        lw.addWheelItems(wheelItems)
        lw.setTarget(1)
        lw.setLuckyWheelReachTheTarget {
            showShortInformation("Target Reached")
        }

        start.setOnClickListener {
            lw.rotateWheelTo(1)
        }
    }

    private fun generateWheelItems() {
        wheelItems.add(
            WheelItem(
                Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_1
                ), "100 $"
            )
        )
        wheelItems.add(
            WheelItem(
                Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_2
                ), "0 $"
            )
        )
        wheelItems.add(
            WheelItem(
                Color.parseColor("#F00E6F"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_3
                ), "30 $"
            )
        )
        wheelItems.add(
            WheelItem(
                Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_4
                ), "6000 $"
            )
        )
        wheelItems.add(
            WheelItem(
                Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_6
                ), "9 $"
            )
        )
        wheelItems.add(
            WheelItem(
                Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(
                    resources, R.drawable.face_7
                ), "20 $"
            )
        )
    }
}