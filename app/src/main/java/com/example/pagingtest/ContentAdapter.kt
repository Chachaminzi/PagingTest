package com.example.pagingtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingtest.databinding.ItemContentListBinding
import com.example.pagingtest.models.Content

class ContentAdapter(private val onClickListener: ContentClickListener) :
    RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    var data = listOf<Content>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(private val binding: ItemContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content, clickListener: ContentClickListener) {
            binding.content = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContentListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onClickListener)
    }

    override fun getItemCount(): Int = data.size

    class ContentClickListener(private val clickListener: (item: Content) -> Unit) {
        fun onClick(item: Content) = clickListener(item)
    }
}