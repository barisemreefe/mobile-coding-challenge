package com.bee.traderev.feed

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.utils.BaseActivity


class PhotoDetailActivity : BaseActivity() {
    @BindView(R.id.photodetail_recyclerview)
    lateinit var recyclerView: RecyclerView
    private val viewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this).get(FeedViewModel::class.java).apply {
            setFeedRepository(FeedRepository())
        }
    }
    private val adapter: PhotosAdapter by lazy {
        PhotosAdapter(Type.PHOTO_DETAIL, photosAdapterListener)
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
    private var position = 0
    private var selectedPhotoId :String? =null
    private var lastPage : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        ButterKnife.bind(this)
        supportPostponeEnterTransition()
        position = intent.getIntExtra(POSITION, -1)
        selectedPhotoId = intent.getStringExtra(ID)
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

        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        snapHelper.attachToRecyclerView(recyclerView)
        viewModel.getFeed()?.observe(this, feedObserver)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var dx = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    localBroadcastManager.sendBroadcast(pageChangedIntent.putExtra(POSITION, position))
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
            lastPage = it.last().pageLocation
            adapter.addItems(it)
            recyclerView.scrollToPosition(position)
        }
    }

    private val photosAdapterListener = object : PhotosAdapter.PhotosAdapterListener {

        override fun loadMore() {
            viewModel.loadMore(lastPage)?.observe(this@PhotoDetailActivity, feedObserver)
        }

        override fun imageLoaded(id: String) {
            super.imageLoaded(id)
            if (isSelectedPhotoLoaded(id)) {
                recyclerView.post {
                    supportStartPostponedEnterTransition()
                }
            }
        }

        override fun onLocationClicked(location: String?) {
            super.onLocationClicked(location)
            location?.let {
                openLocation(it)
            }
        }
    }

    private fun isSelectedPhotoLoaded(id : String) = (selectedPhotoId == id)

    private fun openLocation(location :String) {
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location)).apply {
            `package` = MAPS_PACKAGE
        }
        startActivity(mapIntent)
    }

    companion object {
        private const val MAPS_PACKAGE = "com.google.android.apps.maps"
        const val POSITION = "position"
        const val ID = "id"
        const val PAGE_CHANGED = "pageChanged"
        fun newIntent(context: Context,id: String,position: Int) =
                Intent(context, PhotoDetailActivity::class.java).apply {
                    putExtra(PhotoDetailActivity.POSITION,position)
                    putExtra(PhotoDetailActivity.ID,id)
                }
    }


}

