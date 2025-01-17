package vn.loitp.up.a.cv.layout.expansionPanel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.setSafeOnClickListenerElastic
import vn.loitp.R
import vn.loitp.databinding.AExpansionPanelSampleRecyclerBinding
import vn.loitp.up.a.cv.layout.expansionPanel.ExpansionPanelSampleActivityRecycler.RecyclerAdapter.RecyclerHolder

@LogTag("ExpansionPanelSampleActivityRecycler")
@IsFullScreen(false)
class ExpansionPanelSampleActivityRecycler : BaseActivityFont() {
    private lateinit var binding: AExpansionPanelSampleRecyclerBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AExpansionPanelSampleRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = ExpansionPanelSampleActivityRecycler::class.java.simpleName
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter()
        binding.recyclerView.adapter = adapter
        // fill with empty objects
        val list = ArrayList<String>()
        for (i in 0..29) {
            list.add(System.currentTimeMillis().toString())
        }
        adapter.setItems(list)
    }

    class RecyclerAdapter : RecyclerView.Adapter<RecyclerHolder>() {
        private val list = ArrayList<String>()
        private val expansionsCollection = ExpansionLayoutCollection()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
            return RecyclerHolder.buildFor(parent)
        }

        override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
            holder.bind()
            expansionsCollection.add(holder.expansionLayout)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setItems(items: ArrayList<String>) {
            list.addAll(items)
            notifyDataSetChanged()
        }

        class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var expansionLayout: ExpansionLayout = itemView.findViewById(R.id.expansionLayout)
            fun bind() {
                expansionLayout.collapse(false)
            }

            companion object {
                private const val LAYOUT = R.layout.item_expansion_panel_recycler_cell

                fun buildFor(viewGroup: ViewGroup): RecyclerHolder {
                    return RecyclerHolder(
                        LayoutInflater.from(viewGroup.context).inflate(LAYOUT, viewGroup, false)
                    )
                }
            }
        }

        init {
            expansionsCollection.openOnlyOne(true)
        }
    }
}
