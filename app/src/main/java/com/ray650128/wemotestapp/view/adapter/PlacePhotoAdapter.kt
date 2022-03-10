package com.ray650128.wemotestapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.ItemPlacePhotoBinding

class PlacePhotoAdapter : RecyclerView.Adapter<PlacePhotoAdapter.MyViewHolder>() {

    private lateinit var mContext: Context

    private val mData: ArrayList<String?> = arrayListOf(null, null, null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val viewBinding = ItemPlacePhotoBinding.inflate(inflater, parent, false)
        return MyViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(mData[position])
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = mData.size

    fun updateData(data: List<String>) {
        for (i in mData.indices) {
            if (data.isNullOrEmpty()) {
                mData[i] = null
            } else {
                mData[i] = data[i]
            }
            notifyItemChanged(i)
        }
    }

    fun getPhotoList(): ArrayList<String> {
        val tmpList = ArrayList<String>()
        for (i in mData.indices) {
            if (mData[i] == null) continue
            tmpList.add(mData[i]!!)
        }
        return tmpList
    }

    inner class MyViewHolder(private val binding: ItemPlacePhotoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(urlStr: String?) = binding.apply {
            Glide.with(mContext)
                .load(urlStr)
                .override(250, 250)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(imgPhoto)
        }
    }
}