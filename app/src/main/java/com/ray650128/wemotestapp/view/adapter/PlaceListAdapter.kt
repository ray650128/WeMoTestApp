package com.ray650128.wemotestapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.ItemPlaceListBinding
import com.ray650128.wemotestapp.model.Place

class PlaceListAdapter : ListAdapter<Place, PlaceListAdapter.MyViewHolder>(PlaceListDiffCallback()) {

    var onItemClick: ((data: Place) -> Unit)? = null

    var onItemLongClick: ((data: Place) -> Unit)? = null

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListAdapter.MyViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val viewBinding = ItemPlaceListBinding.inflate(inflater, parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: PlaceListAdapter.MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.apply {
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

    inner class MyViewHolder(private val binding: ItemPlaceListBinding): RecyclerView.ViewHolder(binding.root) {
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
}
