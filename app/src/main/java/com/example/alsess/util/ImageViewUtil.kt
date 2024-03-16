package com.example.alsess.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

fun ImageView.Glide(url : String){
    com.bumptech.glide.Glide.with(context).load(url).into(this)
}
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    url?.let { view.Glide(it) }
}