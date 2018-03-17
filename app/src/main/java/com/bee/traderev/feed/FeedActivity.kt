package com.bee.traderev.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo

class FeedActivity : AppCompatActivity() {
    @BindView(R.id.feed_recyclerview)
    lateinit var recyclerView: RecyclerView
    private val items = ArrayList<Photo>()
    private val viewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this).get(FeedViewModel::class.java).apply {
            setFeedRepository(FeedRepository())
        }
    }
    private val adapter:PhotosAdapter by lazy {
        PhotosAdapter(items,photosAdapterListener)
    }
    private var currentPage = 0 //todo move to viewmodel

    //todo handle screen rotation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        ButterKnife.bind(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,3)
        viewModel.getFeed()?.observe(this,feedObserver)
    }

    private val feedObserver = Observer<List<Photo>> {
        it?.let {
            items.addAll(it)
            adapter.notifyDataSetChanged()
            currentPage++
        }
    }

    private val photosAdapterListener  = object :PhotosAdapter.PhotosAdapterListener{
        override fun onItemClicked(item: Photo) {
            //todo open detail
        }

        override fun loadMore() {
            viewModel.getFeed(currentPage)?.observe(this@FeedActivity,feedObserver)
        }

    }
}
