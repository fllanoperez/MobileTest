package com.example.marvel.utils

import android.widget.ImageView
import androidx.compose.ui.graphics.Color
import androidx.databinding.BindingAdapter
import com.example.marvel.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

@BindingAdapter("app:imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    Picasso.get()
        .load(imageUrl)
        .placeholder( R.drawable.progress_animation )
        .error(R.mipmap.ic_launcher)
        .fit()
        .into(view)
}
