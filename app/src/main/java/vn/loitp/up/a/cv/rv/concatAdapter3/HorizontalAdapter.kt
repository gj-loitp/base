package vn.loitp.up.a.cv.rv.concatAdapter3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.loitp.databinding.VConcatAdapte3HorizontalBinding

class HorizontalAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HorizontalViewHolder>() {
    private var data: List<String> = emptyList()

    companion object {
        const val VIEW_TYPE = 4444
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        return HorizontalViewHolder(
            VConcatAdapte3HorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        holder.bind(data[position], onClick)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }
}
