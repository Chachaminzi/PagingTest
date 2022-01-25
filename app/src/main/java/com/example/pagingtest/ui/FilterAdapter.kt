package com.example.pagingtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingtest.databinding.HeaderContentListBinding

class FilterAdapter(
    private val filterClickListener: FilterClickListener,
    private val spinnerAdapter: AdapterView.OnItemSelectedListener
) : RecyclerView.Adapter<FilterAdapter.HeaderViewHolder>() {
    private var currentPostType: Int = 0

    class HeaderViewHolder private constructor(private val binding: HeaderContentListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            filterClickListener: FilterClickListener,
            itemSelectedListener: AdapterView.OnItemSelectedListener,
            currentPostType: Int
        ) {
            binding.filterClickListener = filterClickListener
            binding.headerSpinner.onItemSelectedListener = itemSelectedListener
            binding.headerSpinner.setSelection(currentPostType)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderContentListBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(
            filterClickListener,
            spinnerAdapter,
            currentPostType
        )
    }

    override fun getItemCount(): Int = 1

    fun submitPostType(newPostType: Int) {
        currentPostType = newPostType
    }
}

class FilterClickListener(private val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}