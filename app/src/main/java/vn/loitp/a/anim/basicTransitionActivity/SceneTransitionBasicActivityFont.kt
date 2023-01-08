package vn.loitp.a.anim.basicTransitionActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.loadGlide
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_animation_scene_transition_basic.*
import vn.loitp.R

@LogTag("SceneTransitionBasicActivity")
@IsFullScreen(false)
class SceneTransitionBasicActivityFont : BaseActivityFont(), AdapterView.OnItemClickListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_animation_scene_transition_basic
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
            this.tvTitle?.text = SceneTransitionBasicActivityFont::class.java.simpleName
        }
        gridView.onItemClickListener = this
        val gridAdapter = GridAdapter()
        gridView.adapter = gridAdapter
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = adapterView.getItemAtPosition(position) as Item
        val intent = Intent(this, SceneTransitionBasicDetailActivityFont::class.java)
        intent.putExtra(SceneTransitionBasicDetailActivityFont.EXTRA_PARAM_ID, item.id)
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,

            // Now we provide a list of Pair items which contain the view we can transitioning
            // from, and the name of the view it is transitioning to, in the launched activity
            Pair(
                view.findViewById(R.id.imageViewItem),
                SceneTransitionBasicDetailActivityFont.VIEW_NAME_HEADER_IMAGE
            ),
            Pair(
                view.findViewById(R.id.textViewName),
                SceneTransitionBasicDetailActivityFont.VIEW_NAME_HEADER_TITLE
            )
        )
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
    }

    private inner class GridAdapter : BaseAdapter() {

        override fun getCount(): Int {
            return Item.ITEMS.size
        }

        override fun getItem(position: Int): Item {
            return Item.ITEMS[position]
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).id.toLong()
        }

        override fun getView(position: Int, v: View?, viewGroup: ViewGroup): View? {
            var view = v
            if (view == null) {
                view = layoutInflater.inflate(R.layout.view_row_item_grid_view, viewGroup, false)
            }

            val item = getItem(position)

            val imageViewItem = view?.findViewById<ImageView>(R.id.imageViewItem)
            imageViewItem.loadGlide(
                any = item.photoUrl,
            )

            val textViewName = view?.findViewById<TextView>(R.id.textViewName)
            textViewName?.text = item.name

            return view
        }
    }
}
