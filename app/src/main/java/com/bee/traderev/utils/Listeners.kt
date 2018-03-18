package com.bee.traderev.utils

import android.view.View

interface ItemClickListener<in T> {
    fun onItemClicked(position: Int,view : View,item : T){}
}

interface LoadMoreListener {
    fun loadMore()
}