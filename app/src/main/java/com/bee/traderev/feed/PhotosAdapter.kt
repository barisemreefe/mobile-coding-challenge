package com.bee.traderev.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bee.traderev.R
import com.bee.traderev.datatypes.Photo
import com.bee.traderev.utils.ItemClickListener
import com.bee.traderev.utils.LoadMoreListener
import com.bee.traderev.utils.PhotoLoadListener
import com.bee.traderev.utils.setPhoto

enum class Type {
    PHOTO_DETAIL, FEED
}

class PhotosAdapter(private val type: Type = Type.FEED, private val items: List<Photo>, private val listener: PhotosAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface PhotosAdapterListener : ItemClickListener<Photo>, LoadMoreListener {
        fun imageLoaded(){}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(getListItem(), parent, false)
        with(getViewHolder(v)) {
            if (Type.FEED == type) {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClicked((this as ViewHolder).photoImageView,items[adapterPosition])
                    }
                }
            }
            return this
        }
    }

    private fun getListItem() = when (type) {
        Type.PHOTO_DETAIL -> R.layout.item_grid_photo
        Type.FEED -> R.layout.item_grid_photo
    }

    private fun getViewHolder(view: View) = when (type) {
        Type.PHOTO_DETAIL -> ViewHolderDetail(view)
        Type.FEED -> ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = items[position]

        when (type) {
            Type.PHOTO_DETAIL -> bindPhotoDetail(holder as ViewHolderDetail, photo)
            Type.FEED -> bindPhoto(holder as ViewHolder, photo)
        }

        if (position + LOAD_MORE_THRESHOLD == items.size) {
            listener.loadMore()
        }
    }

    private fun bindPhoto(holder: ViewHolder, photo: Photo) {
        with(holder) {
            photoImageView.setPhoto(photo)
        }
    }

    private fun bindPhotoDetail(holder: ViewHolderDetail, photo: Photo) {
        with(holder) {
            photoImageView.setPhoto(photo,object :PhotoLoadListener{
                override fun onPhotoLoaded() {
                    listener.imageLoaded()
                }

            })
            descriptionTextView.text = photo.user?.name
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

    class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.item_imageview_photo)
        lateinit var photoImageView: ImageView
        @BindView(R.id.item_textview_description)
        lateinit var descriptionTextView: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    companion object {
        private const val LOAD_MORE_THRESHOLD = 5
    }

}