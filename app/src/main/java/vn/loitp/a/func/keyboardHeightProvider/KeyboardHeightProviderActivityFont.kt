package vn.loitp.a.func.keyboardHeightProvider

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isVisible
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_kb_height_provider.*
import vn.loitp.R

@LogTag("KeyboardHeightProviderActivity")
@IsFullScreen(false)
class KeyboardHeightProviderActivityFont : BaseActivityFont() {

    private var keyboardHeightProvider: KeyboardHeightProvider? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.a_kb_height_provider
    }

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
            this.ivIconRight?.isVisible = false
            this.tvTitle?.text = KeyboardHeightProviderActivityFont::class.java.simpleName
        }
        keyboardHeightProvider = KeyboardHeightProvider(this)
        keyboardHeightProvider?.addKeyboardListener(object :
            KeyboardHeightProvider.KeyboardListener {
            override fun onHeightChanged(height: Int) {
                if (height == 0) {
                    layoutKeyboardFake.isVisible = false
                } else {
                    if (layoutKeyboardFake.layoutParams.height != height) {
                        layoutKeyboardFake.layoutParams.height = height
                        layoutKeyboardFake.requestLayout()
                    }
                    layoutKeyboardFake.isVisible = true
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        keyboardHeightProvider?.onResume()
    }

    public override fun onPause() {
        super.onPause()
        keyboardHeightProvider?.onPause()
    }
}
