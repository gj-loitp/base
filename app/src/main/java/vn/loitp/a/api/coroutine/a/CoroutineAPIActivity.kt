package vn.loitp.a.api.coroutine.a

import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseFontActivity
import vn.loitp.R

@LogTag("CoroutineAPIActivity")
@IsFullScreen(false)
class CoroutineAPIActivity : BaseFontActivity() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_coroutine_api
    }
}
