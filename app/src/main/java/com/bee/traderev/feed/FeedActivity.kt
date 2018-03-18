package com.bee.traderev.feed

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
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
        PhotosAdapter(Type.FEED,items,photosAdapterListener)
    }
    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private var imagePosition : Int= 0
    private var currentPage = 0 //todo move to viewmodel

    //todo handle screen rotation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        ButterKnife.bind(this)
        initializeBroadcastManager()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this,3)
        viewModel.getFeed()?.observe(this,feedObserver)
        setExitSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                        super.onMapSharedElements(names, sharedElements)
                        Log.d("HEYY","shared: $names")
                        val selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(imagePosition!!)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }
                        sharedElements!![names!![0]] = selectedViewHolder.itemView.findViewById(R.id.item_imageview_photo)
                    }
                })
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
            items.addAll(it)
            adapter.notifyDataSetChanged()
            currentPage++
        }
    }

    private val photosAdapterListener  = object :PhotosAdapter.PhotosAdapterListener{
        override fun onItemClicked(position : Int, view: View, item: Photo) {
            imagePosition = position
            val intent = PhotoDetailActivity.newIntent(this@FeedActivity)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@FeedActivity,
                    view,
                    ViewCompat.getTransitionName(view))
            intent.putExtra(PhotoDetailActivity.POSITION,position)
            startActivity(intent, options.toBundle())
//            overridePendingTransition(R.transition.feed_exit_transition,0)
            //todo exclude
        }

        override fun loadMore() {
            viewModel.getFeed(currentPage)?.observe(this@FeedActivity,feedObserver)
        }

    }
}
