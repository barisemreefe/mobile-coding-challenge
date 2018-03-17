package com.bee.traderev.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo

class PhotoDetailActivity : AppCompatActivity() {
    @BindView(R.id.photodetail_recyclerview)
    lateinit var recyclerView: RecyclerView
    private val items = ArrayList<Photo>()
    private val viewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this).get(FeedViewModel::class.java).apply {
            setFeedRepository(FeedRepository())
        }
    }
    private val adapter: PhotosAdapter by lazy {
        PhotosAdapter(Type.PHOTO_DETAIL, items, photosAdapterListener)
    }
    private val snapHelper: SnapHelper by lazy {
        PagerSnapHelper()
    }
    private var currentPage = 0 //todo move to viewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(recyclerView)
        viewModel.getFeed()?.observe(this,feedObserver)

    }

    private val feedObserver = Observer<List<Photo>> {
        it?.let {
            items.addAll(it)
            adapter.notifyDataSetChanged()
            currentPage++
        }
    }

    private val photosAdapterListener = object : PhotosAdapter.PhotosAdapterListener {
        
        override fun loadMore() {
            viewModel.getFeed(currentPage)?.observe(this@PhotoDetailActivity, feedObserver)
        }
    }

    companion object {

        fun newIntent(context: Context) =
                Intent(context, PhotoDetailActivity::class.java)
    }


}

