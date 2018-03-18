package com.bee.traderev.utils

import android.graphics.Color
import android.widget.ImageView
import com.bee.traderev.datatypes.Photo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

interface PhotoLoadListener {
    fun onPhotoLoaded()
}
fun ImageView.setPhoto(photo : Photo,listener: PhotoLoadListener? = null) {
    setBackgroundColor(Color.parseColor(photo.color))
    Picasso.with(context).load(photo.urls!!.regular).into(this,object :Callback{
        override fun onSuccess() {
            listener?.onPhotoLoaded()
        }

        override fun onError() {
            listener?.onPhotoLoaded()
        }

    })
}