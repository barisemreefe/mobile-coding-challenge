package com.bee.traderev.utils

import android.graphics.Color
import android.widget.ImageView
import com.bee.traderev.datatypes.Photo
import com.squareup.picasso.Picasso

fun ImageView.setPhoto(photo : Photo) {
    setBackgroundColor(Color.parseColor(photo.color))
    Picasso.with(context).load(photo.urls!!.regular).into(this)
}