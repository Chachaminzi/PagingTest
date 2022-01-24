package com.example.pagingtest.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pagingtest.R
import com.example.pagingtest.models.Content

@BindingAdapter("imageUrl")
fun bindImageFromUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide
            .with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_image)
            .into(imageView)
    }
}

@BindingAdapter("text")
fun TextView.bindDateFormat(content: Content) {
    text = content.displayText("yyyy년 MM월 dd일")
}