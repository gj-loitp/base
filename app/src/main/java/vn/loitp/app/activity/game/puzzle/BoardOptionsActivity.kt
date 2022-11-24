package vn.loitp.app.activity.game.puzzle

import android.os.Bundle
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import vn.loitp.app.R
import vn.loitp.app.activity.game.puzzle.ui.boardoptions.BoardOptionsFragment

@LogTag("BoardOptionsActivity")
@IsFullScreen(false)
@IsAutoAnimation(true)
class BoardOptionsActivity : BaseFontActivity() {
    override fun setLayoutResourceId(): Int {
        return R.layout.activity_puzzle_board_options
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, BoardOptionsFragment.newInstance()).commitNow()
        }
    }

}
