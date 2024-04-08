package com.example.alsess.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions

fun ImageView.Glide(url : String){
    com.bumptech.glide.Glide.with(context).load(url).apply(RequestOptions().override(200, 200)).into(this)
}
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    url?.let { view.Glide(it) }
}