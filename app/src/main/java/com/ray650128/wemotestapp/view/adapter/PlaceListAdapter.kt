package com.ray650128.wemotestapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.ItemPlaceListBinding
import com.ray650128.wemotestapp.databinding.ItemPlaceListHeaderBinding
import com.ray650128.wemotestapp.model.Place

class PlaceListAdapter : ListAdapterWithHeader<Place, RecyclerView.ViewHolder>(PlaceListDiffCallback()) {

    var onItemClick: ((data: Place) -> Unit)? = null

    var onItemLongClick: ((data: Place) -> Unit)? = null

    private lateinit var mContext: Context

    private var headerItem: String = "列表"

    // Returns the type of view at the given list position
    override fun getItemViewType(position: Int): Int {
        return if (position == HEADER_POSITION) {
            R.layout.item_place_list_header // header layout not included in example
        } else {
            R.layout.item_place_list // data item layout not included in example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        when (viewType) {
            R.layout.item_place_list_header -> {
                val viewBinding = ItemPlaceListHeaderBinding.inflate(inflater, parent, false)
                return PlaceHeaderViewHolder(viewBinding)
            }
            R.layout.item_place_list -> {
                val viewBinding = ItemPlaceListBinding.inflate(inflater, parent, false)
                return PlaceItemViewHolder(viewBinding)
            }
        }
        throw IllegalArgumentException("Unknown view type $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_place_list_header -> {
                (holder as PlaceHeaderViewHolder).bindData(headerItem)
            }
            R.layout.item_place_list -> {
                val data = getItem(position)
                (holder as PlaceItemViewHolder).apply {
                    bindData(data)
                    itemView.setOnClickListener {
                        onItemClick?.invoke(data)
                    }
                    itemView.setOnLongClickListener {
                        onItemLongClick?.invoke(data)
                        true
                    }
                }
            }
        }
    }

    inner class PlaceHeaderViewHolder(private val binding: ItemPlaceListHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: String) = binding.apply {
            textTitle.text = data
        }
    }

    inner class PlaceItemViewHolder(private val binding: ItemPlaceListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Place) = binding.apply {
            textTitle.text = data.title
            textUpdateTime.text = data.updateTime

            val firstPhotoUrl = if (data.photos.isNullOrEmpty()) null else data.photos?.get(0)

            Glide.with(mContext)
                .load(firstPhotoUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .thumbnail(0.1f)
                .into(imgPhoto)
        }
    }

    companion object {
        // The position of the header in the zero-based list
        const val HEADER_POSITION = 0
    }
}
