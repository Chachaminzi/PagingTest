package com.example.pagingtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingtest.databinding.ItemContentListBinding
import com.example.pagingtest.models.Content

class ContentAdapter(
    private val onClickListener: ContentClickListener
) : PagingDataAdapter<Content, ContentAdapter.ItemViewHolder>(ContentDiffCallback) {

    class ItemViewHolder private constructor(private val binding: ItemContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: Content, clickListener: ContentClickListener) {
            binding.item = content
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContentListBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClickListener)
        }
    }
}

object ContentDiffCallback : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }
}

class ContentClickListener(private val clickListener: (item: Content) -> Unit) {
    fun onClick(item: Content) = clickListener(item)
}