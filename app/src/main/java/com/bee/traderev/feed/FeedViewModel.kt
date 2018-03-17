package com.bee.traderev.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.bee.traderev.datatypes.Photo

class FeedViewModel : ViewModel() {
    private var feedRepository: FeedRepository? = null

    fun setFeedRepository(feedRepository: FeedRepository) {
        this.feedRepository = feedRepository
    }

    fun getFeed(page : Int =0): LiveData<List<Photo>>? {
        return feedRepository?.getPhotos(page)
    }

}
