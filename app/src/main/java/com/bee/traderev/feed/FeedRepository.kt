package com.bee.traderev.feed

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.os.Handler
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.db.AppDatabaseProvider
import com.bee.traderev.network.TradeRevRestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedRepository {
    //api occasionally returns 500 & 503
    private var photosCall: Call<List<Photo>>? = null
    private val handler = Handler()

    private fun getFromApi( data : MutableLiveData<List<Photo>>,page : Int) {
        photosCall?.cancel()
        photosCall = TradeRevRestClient.api.getPhotos(page = page)
        photosCall?.enqueue(object : Callback<List<Photo>> {
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful) {
                    val photos = response.body()
                    photos?.let {
                        photos.forEach { it.pageLocation = page }
                        AsyncTask.execute({
                            AppDatabaseProvider.db.photoDao().insertAll(photos)
                        })
                        data.value = it
                    }
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
            }
        })
    }
    fun getPhotos(getPreviousPages: Boolean, page: Int): LiveData<List<Photo>> {
        val data: MutableLiveData<List<Photo>> = MutableLiveData()
        if (getPreviousPages) {
            AsyncTask.execute({
                AppDatabaseProvider.db.photoDao().getAll().let {
                    if (it.isNotEmpty()) {
                        handler.post({
                            data.value = it
                        })
                    } else {
                        getFromApi(data, page)
                    }
                }
            })

        }

        else {
            getFromApi(data, page)
        }

        return data

    }

}