package com.bee.traderev.utils

interface ItemClickListener<in T> {
    fun  onItemClicked(item : T){}
}

interface LoadMoreListener {
    fun loadMore()
}