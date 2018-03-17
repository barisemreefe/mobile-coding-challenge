package com.bee.traderev.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.network.TradeRevRestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedRepository {
    private var photosCall: Call<List<Photo>>? = null

    fun getPhotos(page: Int): LiveData<List<Photo>> {
        val data: MutableLiveData<List<Photo>> = MutableLiveData()

        photosCall?.cancel()
        photosCall = TradeRevRestClient.api.getPhotos(page = page)
        photosCall?.enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                    if (response.isSuccessful) {
                        data.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                }
            })
        return data
    }

}