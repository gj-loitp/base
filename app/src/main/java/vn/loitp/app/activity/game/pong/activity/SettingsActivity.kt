package vn.loitp.app.activity.game.pong.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LScreenUtil
import kotlinx.android.synthetic.main.dialog_settings.*
import vn.loitp.app.R
import vn.loitp.app.activity.game.pong.pong.Difficulty
import vn.loitp.app.activity.game.pong.pong.Mode
import vn.loitp.app.activity.game.pong.pong.Settings
import java.util.*

@LogTag("SettingsActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class SettingsActivity : BaseFontActivity() {

    private val settings = Settings()

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_pong_settings
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        LScreenUtil.setScreenOrientation(this, false)
        LScreenUtil.hideDefaultControls(this)
//        setCustomStatusBar(Color.BLACK, Color.BLACK)

        ArrayAdapter.createFromResource(this, R.array.modeTypes, R.layout.custom_spinner)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.custom_row)
                // Apply the adapter to the spinner
                pvpSpinner.adapter = adapter
            }

        ArrayAdapter.createFromResource(this, R.array.difficulty_types, R.layout.custom_spinner)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.custom_row)
                // Apply the adapter to the spinner
                difficultySpinner.adapter = adapter
            }

        pvpSpinner.onItemSelectedListener = this.ModeSpinnerHandler()
        difficultySpinner.onItemSelectedListener = this.DifficultySpinnerHandler()
    }

    @Suppress("unused")
    fun play(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("settings", settings)
        startActivity(intent)
    }

    private inner class ModeSpinnerHandler : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            settings.pvp = Mode.valueOf(
                parent.getItemAtPosition(position).toString().uppercase(Locale.ROOT).replace(
                    ' ',
                    '_'
                )
            )
        }
    }

    private inner class DifficultySpinnerHandler : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            settings.difficulty =
                Difficulty.valueOf(
                    parent.getItemAtPosition(position).toString()
                        .uppercase(Locale.ROOT)
                )
        }
    }
}