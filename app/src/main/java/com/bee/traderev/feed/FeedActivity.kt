package com.bee.traderev.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo




class FeedActivity : AppCompatActivity() {
    @BindView(R.id.feed_recyclerview)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.feed_progressbar)
    lateinit var loadingView: View
    private val viewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this).get(FeedViewModel::class.java).apply {
            setFeedRepository(FeedRepository())
        }
    }
    private val feedAdapter:PhotosAdapter by lazy {
        PhotosAdapter(Type.FEED,photosAdapterListener)
    }
    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private var imagePosition : Int= 0
    private var lastPage : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        ButterKnife.bind(this)
        initializeBroadcastManager()
        with(recyclerView) {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(this@FeedActivity,resources.getInteger(R.integer.span_count))
        }
        viewModel.getFeed()?.observe(this,feedObserver)
        setExitSharedElementCallback(sharedElementCallback)
    }

    private val sharedElementCallback= object : SharedElementCallback(){
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            super.onMapSharedElements(names, sharedElements)
            val selectedViewHolder = recyclerView
                    .findViewHolderForAdapterPosition(imagePosition)
            if (selectedViewHolder?.itemView == null) {
                return
            }
            if (sharedElements !=null) {
                sharedElements[names!![0]] = selectedViewHolder.itemView.findViewById(R.id.item_imageview_photo)
            }

        }
    }
    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun initializeBroadcastManager() {
        with(IntentFilter()) {
            addAction(PhotoDetailActivity.PAGE_CHANGED)
            localBroadcastManager.registerReceiver(broadcastReceiver, this)
        }
    }
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                PhotoDetailActivity.PAGE_CHANGED -> {
                    imagePosition = intent.getIntExtra(PhotoDetailActivity.POSITION,-1)
                    recyclerView.scrollToPosition(imagePosition)
                }
            }
        }
    }

    private val feedObserver = Observer<List<Photo>> {
        it?.let {
            loadingView.visibility = View.GONE
            lastPage = it.last().pageLocation
            feedAdapter.addItems(it)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        //todo span count can be changed
    }

    private val photosAdapterListener  = object :PhotosAdapter.PhotosAdapterListener{
        override fun onItemClicked(position : Int, view: View, item: Photo) {
            imagePosition = position
            val intent = PhotoDetailActivity.newIntent(this@FeedActivity,item.id,position)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@FeedActivity,
                    view,
                    ViewCompat.getTransitionName(view))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle())
            }else {
                startActivity(intent)
            }
        }

        override fun loadMore() {
            viewModel.loadMore(lastPage)?.observe(this@FeedActivity,feedObserver)
        }

    }
}
