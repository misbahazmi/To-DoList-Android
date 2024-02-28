package com.nytimes.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.misbah.todo.R


/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
// important code for loading image here
@BindingAdapter("cover")
fun loadImage(imageView: ImageView, imageURL: String?) {
    Glide.with(imageView.context)
        .setDefaultRequestOptions(RequestOptions())
        .load(imageURL)
        .circleCrop()
        .placeholder(R.drawable.ic_image_default)
        .into(imageView)
}

@BindingAdapter("banner")
fun loadBanner(imageView: ImageView, imageURL: String?) {
    Glide.with(imageView.context)
        .setDefaultRequestOptions(RequestOptions())
        .load(imageURL)
        .placeholder(R.drawable.ic_image_banner_def)
        .into(imageView)
}