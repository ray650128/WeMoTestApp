package com.ray650128.wemotestapp.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ray650128.wemotestapp.model.Place


class PlaceListDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}