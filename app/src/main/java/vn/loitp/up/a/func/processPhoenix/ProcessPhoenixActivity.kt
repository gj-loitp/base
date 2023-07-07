package vn.loitp.up.a.func.processPhoenix

import android.os.Bundle
import androidx.core.view.isVisible
import com.jakewharton.processphoenix.ProcessPhoenix
import com.loitp.annotation.IsAutoAnimation
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.openUrlInBrowser
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import vn.loitp.R
import vn.loitp.databinding.AProcessPhoenixBinding

@LogTag("ProcessPhoenixActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class ProcessPhoenixActivity : BaseActivityFont() {

    private lateinit var binding: AProcessPhoenixBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AProcessPhoenixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.apply {
                this.setSafeOnClickListenerElastic(runnable = {
                    context.openUrlInBrowser(
                        url = "https://github.com/JakeWharton/ProcessPhoenix"
                    )
                })
                isVisible = true
                setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.tvTitle?.text = ProcessPhoenixActivity::class.java.simpleName
        }

        binding.btStart.setSafeOnClickListener {
            ProcessPhoenix.triggerRebirth(this)
        }
    }
}
