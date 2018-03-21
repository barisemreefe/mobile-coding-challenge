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

class PhotosAdapter(private val type: Type = Type.FEED, private val listener: PhotosAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ArrayList<Photo>()

    interface PhotosAdapterListener : ItemClickListener<Photo>, LoadMoreListener {
        fun imageLoaded(id: String) {}
        fun onLocationClicked(location: String?) {}
    }
    //todo Paging library can be implemented instead of this https://developer.android.com/topic/libraries/architecture/paging.html

    fun setItems(photos: List<Photo>) {
        items.clear()
        addItems(photos)
    }

    fun addItems(photos: List<Photo>) {
        items.addAll(photos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(getListItem(), parent, false)
        val vh = getViewHolder(v)
        if (Type.FEED == type) {
            with(vh as ViewHolder) {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClicked(adapterPosition, this.photoImageView, items[adapterPosition])
                    }
                }
            }

        } else {
            with(vh as ViewHolderDetail) {
                locationTextView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onLocationClicked(items[adapterPosition].user?.location)
                    }
                }
            }
        }
        return vh
    }

    private fun getListItem() = when (type) {
        Type.PHOTO_DETAIL -> R.layout.item_detail_photo
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
            val user = photo.user
            photoImageView.setPhoto(photo, object :PhotoLoadListener{
                override fun onPhotoLoaded(id: String) {
                    listener.imageLoaded(id)
                }

            })
            descriptionTextView.text = user?.name
            twitterTextView.setTextOrHide(user?.twitterUserName)
            instagramTextView.setTextOrHide(user?.instagramUserName)
            locationTextView.setTextOrHide(user?.location)
            bioTextView.setTextOrHide(user?.bio)
            locationImageView.visibility = if (user?.location.isNullOrEmpty()) View.GONE else View.VISIBLE
            bioImageView.visibility = if (user?.bio.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun TextView.setTextOrHide(value: String?) {
        if (value.isNullOrEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            text = value
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
        @BindView(R.id.item_textview_twitterusername)
        lateinit var twitterTextView: TextView
        @BindView(R.id.item_textview_instagramusername)
        lateinit var instagramTextView: TextView
        @BindView(R.id.item_textview_bio)
        lateinit var bioTextView: TextView
        @BindView(R.id.item_textview_location)
        lateinit var locationTextView: TextView
        @BindView(R.id.item_imageview_location)
        lateinit var locationImageView: View
        @BindView(R.id.item_imageview_bio)
        lateinit var bioImageView: View

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    companion object {
        private const val LOAD_MORE_THRESHOLD = 5
    }

}