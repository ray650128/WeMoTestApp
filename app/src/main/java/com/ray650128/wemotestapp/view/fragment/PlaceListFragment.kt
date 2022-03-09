package com.ray650128.wemotestapp.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ray650128.wemotestapp.databinding.FragmentPlaceListBinding
import com.ray650128.wemotestapp.view.adapter.PlaceListAdapter
import com.ray650128.wemotestapp.viewModel.PlaceListViewModel


class PlaceListFragment : BaseFragment<FragmentPlaceListBinding>() {

    private var listAdapter: PlaceListAdapter = PlaceListAdapter()

    private val viewModel: PlaceListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

        initObserver()

        binding.floatingActionButton.setOnClickListener {

        }
    }

    private fun initList() = binding.apply {
        listPlace.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        listAdapter.onItemClick = {

        }
    }

    private fun initObserver() {
        viewModel.listData.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}