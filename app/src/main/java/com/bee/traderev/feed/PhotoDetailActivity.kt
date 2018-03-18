package com.bee.traderev.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View
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
    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private val pageChangedIntent: Intent by lazy {
        Intent(PAGE_CHANGED)
    }
    private var currentPage = 0 //todo move to viewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        supportPostponeEnterTransition()
        val position = intent.getIntExtra(POSITION,-1)
        setEnterSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                        super.onMapSharedElements(names, sharedElements)
                        val selectedViewHolder = recyclerView
                                .findViewHolderForAdapterPosition(position)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }
                        sharedElements!![names!![0]] = selectedViewHolder.itemView.findViewById(R.id.item_imageview_photo)
                    }
                })

        ButterKnife.bind(this)
        recyclerView.adapter = adapter
        val layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(recyclerView)
        viewModel.getFeed()?.observe(this, feedObserver)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var dx = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                   localBroadcastManager.sendBroadcast(pageChangedIntent.putExtra(POSITION,position))
                }

            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this.dx = dx
            }
        })

    }

    private val feedObserver = Observer<List<Photo>> {
        it?.let {
            items.addAll(it)
            adapter.notifyDataSetChanged()
            currentPage++
            //todo
            recyclerView.scrollToPosition(intent.getIntExtra(POSITION,-1))
        }
    }

    private val photosAdapterListener = object : PhotosAdapter.PhotosAdapterListener {

        override fun loadMore() {
            viewModel.getFeed(currentPage)?.observe(this@PhotoDetailActivity, feedObserver)
        }

        override fun imageLoaded() {
            super.imageLoaded()
            supportStartPostponedEnterTransition()
        }
    }

    companion object {
        const val POSITION = "position"
        const val PAGE_CHANGED = "pageChanged"
        fun newIntent(context: Context) =
                Intent(context, PhotoDetailActivity::class.java)
    }


}

