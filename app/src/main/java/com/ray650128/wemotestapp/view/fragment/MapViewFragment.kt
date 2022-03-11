package com.ray650128.wemotestapp.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.FragmentMapViewBinding
import com.ray650128.wemotestapp.model.Place
import com.ray650128.wemotestapp.viewModel.PlaceListViewModel


class MapViewFragment : BaseFragment<FragmentMapViewBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val viewModel: PlaceListViewModel by activityViewModels()

    private val navController: NavController by lazy { findNavController() }

    private val markerOptionsGreen: MarkerOptions by lazy {
        MarkerOptions().apply {
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).alpha(1f)
        }
    }

    private val markerOptionsRed: MarkerOptions by lazy {
        MarkerOptions()
    }

    private var placeList: ArrayList<Place> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapViewFragment)

        initObserver()
    }

    private fun initObserver() {
        viewModel.listData.observe(viewLifecycleOwner) { list ->
            placeList = ArrayList(list)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        mMap.isMyLocationEnabled = true

        mMap.setOnMarkerClickListener { marker ->
            val action = MapViewFragmentDirections.mapToPlaceDetail(
                editMode = PlaceFragment.VIEW_MODE,
                dataId = marker.tag.toString().toInt()
            )
            navController.navigate(action)
            false
        }

        initLocation()
        createLocationRequest()

        showMarker()
    }

    // 初始化位置，由於已經先在onMapReady()中要求權限了，因此無需再次要求權限
    @SuppressLint("MissingPermission")
    private fun initLocation() {
        val client = LocationServices.getFusedLocationProviderClient(requireActivity())

        client.lastLocation.addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                val location = task.result ?: return@addOnCompleteListener
                val latLng = LatLng(location.latitude, location.longitude)
                val camera = CameraUpdateFactory.newLatLngZoom(latLng, mMap.maxZoomLevel)
                mMap.animateCamera(camera)
            }
        }
    }

    // 設定位置要求的參數
    @SuppressLint("RestrictedApi")
    private fun createLocationRequest() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun showMarker() {
        for (item in placeList) {
            val latLng = LatLng(item.latitude, item.longitude)
            if (item.photos.isNullOrEmpty()) {
                markerOptionsRed.position(latLng)
                markerOptionsRed.title(item.title)
                mMap.addMarker(markerOptionsRed).apply {
                    this?.tag = item.id
                }
            } else {
                markerOptionsGreen.position(latLng)
                markerOptionsGreen.title(item.title)
                mMap.addMarker(markerOptionsGreen).apply {
                    this?.tag = item.id
                }
            }
        }
    }
}