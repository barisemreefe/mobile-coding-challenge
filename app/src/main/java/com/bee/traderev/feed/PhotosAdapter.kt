package com.bee.traderev.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.utils.ItemClickListener
import com.bee.traderev.utils.LoadMoreListener
import com.bee.traderev.utils.setPhoto

class PhotosAdapter(private val items: List<Photo>, private val listener: PhotosAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PhotosAdapterListener : ItemClickListener<Photo>,LoadMoreListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(
                         R.layout.item_grid_photo, parent, false)
        val vh = ViewHolder(v)
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = items[position]
        bindPhoto(holder as ViewHolder,photo)

        if (position + LOAD_MORE_THRESHOLD == items.size) {
            listener.loadMore()
        }
    }

    private fun bindPhoto(holder: ViewHolder, photo: Photo) {
        with(holder) {
            photoImageView.setPhoto(photo)
        }
    }

    override fun getItemCount() = items.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.item_imageview_photo)
        lateinit var photoImageView: ImageView

        init {
            ButterKnife.bind(this, itemView)

        }
    }

    companion object {
        private const val LOAD_MORE_THRESHOLD = 5
    }

}