package com.bee.traderev.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bee.traderev.datatypes.Photo

class FeedViewModel : ViewModel() {
    private var feedRepository: FeedRepository? = null
    private var page : Int = 1

    fun setFeedRepository(feedRepository: FeedRepository) {
        this.feedRepository = feedRepository
    }

    fun getFeed(): LiveData<List<Photo>>? {
        return feedRepository?.getPhotos(true,page++)
    }
    fun loadMore(lastPage : Int): LiveData<List<Photo>>? {
        this.page = lastPage+1
        return feedRepository?.getPhotos(false,page)
    }
}
