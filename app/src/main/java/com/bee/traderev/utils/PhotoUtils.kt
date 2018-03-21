package com.bee.traderev.utils

import android.graphics.Color
import android.widget.ImageView
import com.bee.traderev.datatypes.Photo
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

interface PhotoLoadListener {
    fun onPhotoLoaded(id:String)
}
fun ImageView.setPhoto(photo : Photo,listener: PhotoLoadListener? = null) {
    setBackgroundColor(Color.parseColor(photo.color))
    Picasso.with(context).load(photo.urls!!.thumb).into(this,object :Callback{
        override fun onSuccess() {
            listener?.onPhotoLoaded(photo.id)
        }

        override fun onError() {
            listener?.onPhotoLoaded(photo.id)
        }

    })
}