package vn.loitp.app.activity.customviews.recyclerview.turnLayoutManager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import cdflynn.android.library.turn.TurnLayoutManager
import com.loitpcore.annotation.IsAutoAnimation
import com.loitpcore.annotation.IsFullScreen
import com.loitpcore.annotation.LogTag
import com.loitpcore.core.base.BaseFontActivity
import com.loitpcore.core.utilities.LSocialUtil
import com.loitpcore.core.utilities.LUIUtil
import kotlinx.android.synthetic.main.activity_0.*
import kotlinx.android.synthetic.main.activity_0.lActionBar
import kotlinx.android.synthetic.main.activity_turn_layout_manager.*
import kotlinx.android.synthetic.main.activity_video_exo_player2.*
import kotlinx.android.synthetic.main.view_controls_tlm.*
import vn.loitp.app.R

@LogTag("TurnLayoutManagerActivity")
@IsFullScreen(false)
@IsAutoAnimation(false)
class TurnLayoutManagerActivity : BaseFontActivity() {

    private var layoutManager: TurnLayoutManager? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_turn_layout_manager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(view = this.ivIconLeft, runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.let {
                LUIUtil.setSafeOnClickListenerElastic(view = it, runnable = {
                    LSocialUtil.openUrlInBrowser(
                        context = context, url = "https://github.com/cdflynn/turn-layout-manager"
                    )
                })
                it.isVisible = true
                it.setImageResource(R.drawable.ic_baseline_code_48)
            }
            this.viewShadow?.isVisible = true
            this.tvTitle?.text = TurnLayoutManagerActivity::class.java.simpleName
        }

        val adapter = SampleAdapter(this)
        val radius = resources.getDimension(R.dimen.list_radius).toInt()
        val peek = resources.getDimension(R.dimen.list_peek).toInt()
        layoutManager = TurnLayoutManager(
            /* context = */ this,
            /* gravity = */ TurnLayoutManager.Gravity.START,
            /* orientation = */ TurnLayoutManager.Orientation.VERTICAL,
            /* radius = */ radius,
            /* peekDistance = */ peek,
            /* rotate = */ rotate.isChecked
        )
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter
        adapter.notifyDataSetChanged()
        seek_radius.setOnSeekBarChangeListener(radiusListener)
        seek_peek.setOnSeekBarChangeListener(peekListener)
        seek_radius.progress = radius
        seek_peek.progress = peek
        gravity.onItemSelectedListener = gravityOptionsClickListener
        orientation.onItemSelectedListener = orientationOptionsClickListener
        gravity.adapter = GravityAdapter(this, R.layout.view_spinner_item_tlm)
        orientation.adapter = OrientationAdapter(this, R.layout.view_spinner_item_tlm)
        rotate.setOnCheckedChangeListener(rotateListener)
        control_handle.setOnClickListener(controlsHandleClickListener)
    }

    private val radiusListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            radius_text.text = resources.getString(R.string.radius_format, progress)
            if (fromUser) {
                layoutManager?.setRadius(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // do nothing
        }
    }

    private val peekListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            peek_text.text = resources.getString(R.string.peek_format, progress)
            if (fromUser) {
                layoutManager?.setPeekDistance(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            // do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // do nothing
        }
    }

    private val orientationOptionsClickListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        layoutManager?.orientation = TurnLayoutManager.VERTICAL
                        return
                    }
                    1 -> layoutManager?.orientation = TurnLayoutManager.HORIZONTAL
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    private val gravityOptionsClickListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        layoutManager?.setGravity(TurnLayoutManager.Gravity.START)
                        return
                    }
                    1 -> layoutManager?.setGravity(TurnLayoutManager.Gravity.END)
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    private val rotateListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        layoutManager?.setRotate(
            isChecked
        )
    }

    private val controlsHandleClickListener = View.OnClickListener {
        val translationY = if (controls.translationY == 0f) controls.height.toFloat() else 0f
        controls.animate().translationY(translationY).start()
        control_handle.animate().translationY(translationY).start()
    }

    private class OrientationAdapter(
        context: Context, @LayoutRes resource: Int
    ) : ArrayAdapter<String?>(context, resource, arrayOf("Vertical", "Horizontal"))

    private class GravityAdapter(
        context: Context, @LayoutRes resource: Int
    ) : ArrayAdapter<String?>(context, resource, arrayOf("Start", "End"))
}
