package com.ray650128.wemotestapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.FragmentPlaceListBinding
import com.ray650128.wemotestapp.model.Place
import com.ray650128.wemotestapp.view.adapter.PlaceListAdapter
import com.ray650128.wemotestapp.viewModel.PlaceListViewModel
import io.realm.RealmList


class PlaceListFragment : BaseFragment<FragmentPlaceListBinding>() {

    private var listAdapter: PlaceListAdapter = PlaceListAdapter()

    private val viewModel: PlaceListViewModel by activityViewModels()

    private val navController: NavController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

        initObserver()

        binding.floatingActionButton.setOnClickListener {
            val action = PlaceListFragmentDirections.listToPlaceDetail(
                editMode = PlaceFragment.ADD_MODE,
                dataId = -1
            )
            navController.navigate(action)
        }
    }

    /**
     * 初始化列表
     */
    private fun initList() = binding.apply {
        listPlace.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        listAdapter.onItemClick = {
            val action = PlaceListFragmentDirections.listToPlaceDetail(
                editMode = PlaceFragment.VIEW_MODE,
                dataId = it.id
            )
            navController.navigate(action)
        }

        listAdapter.onItemLongClick = {
            showPlaceMenu(it)
        }
    }

    /**
     * 顯示列表項目功能表
     * @param item  選中的項目
     */
    private fun showPlaceMenu(item: Place) {
        val popupMenu = AlertDialog.Builder(requireContext()).apply {
            setTitle(item.title)
            setCancelable(true)
            setItems(R.array.place_menu) { dialog, which ->
                when (which) {
                    0 -> gotoEditFragment(item.id)
                    1 -> viewModel.deleteData(item.id)
                }
                dialog.dismiss()
            }
        }
        popupMenu.show()
    }

    /**
     * 跳轉到編輯頁面
     * @param id    項目的id
     */
    private fun gotoEditFragment(id: Int) {
        val action = PlaceListFragmentDirections.listToPlaceDetail(
            editMode = PlaceFragment.EDIT_MODE,
            dataId = id
        )
        navController.navigate(action)
    }

    /**
     * 初始化 observer
     */
    private fun initObserver() {
        viewModel.listData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}