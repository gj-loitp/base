package vn.loitp.up.a.cv.layout.autoScrollContent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vn.loitp.databinding.ItemAutoscrollContentBinding

class ExampleAdapter : ListAdapter<ExampleModel, ViewHolder>(ExampleDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemAutoscrollContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ExampleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val itemViewHolder = holder as ExampleViewHolder
        itemViewHolder.bind(getItem(position))
    }

    fun onLinkItem(
        holder: ViewHolder,
        position: Int,
        onLinkItem: (String) -> Unit
    ) {
        val itemViewHolder = holder as ExampleViewHolder
        itemViewHolder.onLinkItem(item = getItem(position), onLinkItem = onLinkItem)
    }
}

class ExampleDiffCallback : DiffUtil.ItemCallback<ExampleModel>() {

    override fun areItemsTheSame(
        oldItem: ExampleModel,
        newItem: ExampleModel
    ): Boolean =
        oldItem.message == newItem.message

    override fun areContentsTheSame(
        oldItem: ExampleModel,
        newItem: ExampleModel
    ): Boolean =
        oldItem == newItem
}
