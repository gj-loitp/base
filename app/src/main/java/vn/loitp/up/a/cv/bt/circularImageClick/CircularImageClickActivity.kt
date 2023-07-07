package vn.loitp.up.a.cv.bt.circularImageClick

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.views.bt.circularImageClick.LCircularClickImageButton
import vn.loitp.databinding.ACircularImageClickBinding

@LogTag("CircularImageClickActivity")
@IsFullScreen(false)
class CircularImageClickActivity : BaseActivityFont() {
    private lateinit var binding: ACircularImageClickBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ACircularImageClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = CircularImageClickActivity::class.java.simpleName
        }
        binding.circleButton.setOnCircleClickListener(object :
            LCircularClickImageButton.OnCircleClickListener {
            override fun onClick(v: View?) {
                showShortInformation("onClick")
            }
        })
    }
}
