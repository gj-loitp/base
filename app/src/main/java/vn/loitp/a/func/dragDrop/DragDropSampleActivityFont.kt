package vn.loitp.a.func.dragDrop

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.View.OnDragListener
import android.widget.ImageView
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_func_drag_drop_sample.*
import vn.loitp.R

@LogTag("DragDropSampleActivity")
@IsFullScreen(false)
class DragDropSampleActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_func_drag_drop_sample
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
            this.tvTitle?.text = DragDropSampleActivityFont::class.java.simpleName
        }

        ivPaper.tag = "paper"
        ivTrash.tag = "trash"

        ivTrash.setOnDragListener(
            TrashDragListener(
                enterShape = R.drawable.ic_launcher_loitp,
                normalShape = R.drawable.ic_search_black_48dp
            )
        )
        ivTrash.setSafeOnClickListener {
            ivPaper.visibility = View.VISIBLE
        }
        ivPaper.setOnLongClickListener { view: View ->
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(view)
            view.startDrag(data, shadowBuilder, view, 0)
            view.visibility = View.INVISIBLE
            true
        }
    }

    private class TrashDragListener(private val enterShape: Int, private val normalShape: Int) :
        OnDragListener {
        private var hit = false
        override fun onDrag(v: View, event: DragEvent): Boolean {
            val containerView = v as ImageView
            val draggedView = event.localState as ImageView
            return when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    hit = false
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    containerView.setImageResource(enterShape)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    containerView.setImageResource(normalShape)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    hit = true
                    draggedView.post {
                        draggedView.visibility = View.GONE
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    containerView.setImageResource(normalShape)
                    v.setVisibility(View.VISIBLE)
                    if (!hit) {
                        draggedView.post {
                            draggedView.visibility = View.VISIBLE
                        }
                    }
                    true
                }
                else -> true
            }
        }
    }
}
