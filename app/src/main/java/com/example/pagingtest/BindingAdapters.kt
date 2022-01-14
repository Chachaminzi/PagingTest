package com.example.pagingtest

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("imageUrl")
fun bindImageFromUrl(imageView: ImageView, url: String?) {
    url?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide
            .with(imageView.context)
            .load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_image)
            .into(imageView)
    }
}

@BindingAdapter("text")
fun TextView.bindDateFormat(string: String?) {
    text = string?.let {
        convertStringToDateString(string)
    }
}