package com.ray650128.wemotestapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import com.google.android.material.tabs.TabLayout
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private val navController: NavController by lazy {
        findNavController(this@MainActivity, R.id.main_fragment)
    }

    private val navOptions: NavOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        initTabLayout()
    }

    private fun initTabLayout() = binding.apply {
        val listTab = tabLayout.newTab().apply {
            text = "列表"
        }
        val listMap = tabLayout.newTab().apply {
            text = "地圖"
        }
        tabLayout.apply {
            addTab(listTab)
            addTab(listMap)
            selectTab(listTab)
            addOnTabSelectedListener(this@MainActivity)
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position) {
            0 -> navController.navigate(R.id.toPlaceList, null, navOptions)
            1 -> navController.navigate(R.id.toMapView, null, navOptions)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    override fun onBackPressed() {
        binding.tabLayout.getTabAt(0)?.select()
        super.onBackPressed()
    }
}