package com.ray650128.wemotestapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class LocationUtil(private val activity: Activity) {

    var onInitialSuccess: ((task: Task<Location>) -> Unit)? = null

    init {
        initLocation()
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        val client = LocationServices.getFusedLocationProviderClient(activity)

        client.lastLocation.addOnCompleteListener(activity) { task ->
            onInitialSuccess?.invoke(task)
        }
    }
}