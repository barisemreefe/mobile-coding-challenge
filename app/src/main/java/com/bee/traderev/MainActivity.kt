package com.bee.traderev

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.network.TradeRevRestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TradeRevRestClient.api.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onFailure(call: Call<List<Photo>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<List<Photo>>?, response: Response<List<Photo>>?) {
                Log.d("HEYY",response?.body()?.map { it.urls?.regular }.toString())
            }

        })

    }
}
