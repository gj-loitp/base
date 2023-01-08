package vn.loitp.a.cv.sb.boxedVertical

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import android.graphics.Color
import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_sb_boxed_vertical.*
import vn.loitp.R

// https://github.com/alpbak/BoxedVerticalSeekBar?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6291
@LogTag("BoxedVerticalSeekBarActivity")
@IsFullScreen(false)
class BoxedVerticalSeekBarActivityFont : BaseActivityFont() {
    private val stringList = ArrayList<String>()

    override fun setLayoutResourceId(): Int {
        return R.layout.a_sb_boxed_vertical
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = BoxedVerticalSeekBarActivityFont::class.java.simpleName
        }

        boxedVertical.setOnBoxedPointsChangeListener(object : BoxedVertical.OnValuesChangeListener {
            override fun onPointsChanged(boxedPoints: BoxedVertical, value: Int) {
                logD("onPointsChanged $value")
                stringList.add(index = 0, element = "onPointsChanged $value")
                print()
            }

            override fun onStartTrackingTouch(boxedPoints: BoxedVertical) {
                logD("onStartTrackingTouch")
                stringList.add(index = 0, element = "onStartTrackingTouch")
                print()
            }

            override fun onStopTrackingTouch(boxedPoints: BoxedVertical) {
                logD("onStopTrackingTouch")
                stringList.add(index = 0, element = "onStopTrackingTouch")
                print()
                boxedVertical.setBackgroundColor(Color.RED)
            }
        })
    }

    private fun print() {
        if (stringList.size > 30) {
            stringList.removeAt(stringList.size - 1)
        }
        var x = ""
        for (s in stringList) {
            x += "\n" + s
        }
        textView?.text = x
    }
}
