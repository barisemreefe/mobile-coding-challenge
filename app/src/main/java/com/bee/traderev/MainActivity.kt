package com.bee.traderev

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.feed.PhotosAdapter
import com.bee.traderev.network.TradeRevRestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    @BindView(R.id.main_recyclerview)
    lateinit var recyclerView: RecyclerView
    private val items = ArrayList<Photo>()
    private val adapter:PhotosAdapter by lazy {
        PhotosAdapter(items,object :PhotosAdapter.PhotosAdapterListener{
            override fun loadMore() {

            }

            override fun onItemClicked(position: Int) {

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        TradeRevRestClient.api.getPhotos().enqueue(object : Callback<List<Photo>> {
            override fun onFailure(call: Call<List<Photo>>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<List<Photo>>?, response: Response<List<Photo>>?) {
                Log.d("HEYY",response?.body()?.map { it.urls?.regular }.toString())
                items.addAll(response?.body()!!)
                adapter.notifyDataSetChanged()
            }
        })

    }
}
