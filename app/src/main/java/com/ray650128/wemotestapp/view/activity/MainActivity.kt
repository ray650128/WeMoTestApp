package com.ray650128.wemotestapp.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.google.android.material.tabs.TabLayout
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.ActivityMainBinding
import com.ray650128.wemotestapp.util.PermissionUtil
import com.ray650128.wemotestapp.viewModel.PlaceListViewModel

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(this@MainActivity, R.id.main_fragment)
    }

    private val viewModel: PlaceListViewModel by viewModels()

    private val navOptions: NavOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()
    }

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        initTabLayout()

        viewModel.isPlaceDetailShow.observe(this) { isShow ->
            if (isShow) {
                binding.tabLayout.isVisible = false
                binding.toolbar.isVisible = true
            } else {
                binding.tabLayout.isVisible = true
                binding.toolbar.isVisible = false
            }
        }

        PermissionUtil.checkPermission(this, permissions, object : PermissionUtil.PermissionResultCallback {
            override fun onGrant() {}

            override fun onDeny(denies: ArrayList<String>?) {}
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    /**
     * 初始化 TabLayout
     */
    private fun initTabLayout() = binding.apply {
        val listTab = tabLayout.newTab().apply {
            text = getString(R.string.text_button_list)
        }
        val listMap = tabLayout.newTab().apply {
            text = getString(R.string.text_button_map)
        }
        tabLayout.apply {
            addTab(listTab)
            addTab(listMap)
            selectTab(listTab)
            addOnTabSelectedListener(this@MainActivity)
        }
    }

    /**
     * TabLayout.OnTabSelectedListener.onTabSelected
     */
    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position) {
            0 -> navController.navigate(R.id.toPlaceList, null, navOptions)
            1 -> navController.navigate(R.id.toMapView, null, navOptions)
        }
    }

    /**
     * TabLayout.OnTabSelectedListener.onTabUnselected
     */
    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    /**
     * TabLayout.OnTabSelectedListener.onTabReselected
     */
    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onBackPressed() {
        binding.tabLayout.getTabAt(0)?.select()
        super.onBackPressed()
    }
}