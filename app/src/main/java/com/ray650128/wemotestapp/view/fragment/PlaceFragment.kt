package com.ray650128.wemotestapp.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ray650128.wemotestapp.R
import com.ray650128.wemotestapp.databinding.FragmentPlaceBinding
import com.ray650128.wemotestapp.databinding.FragmentPlaceEditModeBinding
import com.ray650128.wemotestapp.model.Place
import com.ray650128.wemotestapp.view.adapter.PlacePhotoAdapter
import com.ray650128.wemotestapp.viewModel.PlaceListViewModel
import io.realm.RealmList
import java.text.SimpleDateFormat


class PlaceFragment : Fragment() {

    private val args: PlaceFragmentArgs by navArgs()

    private var editMode = 0

    private var dataId = -1

    private var binding: ViewBinding? = null

    private val viewModel: PlaceListViewModel by activityViewModels()

    private var photoList: ArrayList<String> = arrayListOf()

    private var photoListAdapter: PlacePhotoAdapter = PlacePhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        editMode = args.editMode
        dataId = args.dataId

        viewModel.getData(dataId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = when (editMode) {
            VIEW_MODE -> FragmentPlaceBinding.inflate(inflater, container, false)
            else -> FragmentPlaceEditModeBinding.inflate(inflater, container, false)
        }

        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.placeData.observe(viewLifecycleOwner) {
            when (editMode) {
                VIEW_MODE -> viewModeCase(it)
                EDIT_MODE -> editModeCase(it)
                else -> addModeCase()
            }
        }
    }

    private fun viewModeCase(data: Place?) = (binding as FragmentPlaceBinding).apply {
        if (data == null) return@apply
        textTitle.text = data.title
        textUpdateDate.text = data.updateTime
        textContent.text = data.content

        photoList = ArrayList(data.photos?.toList() ?: arrayListOf())

        photoListAdapter.updateData(photoList)

        listPhoto.apply {
            adapter = photoListAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        btnAction.text = requireActivity().getString(R.string.text_button_close)
        btnAction.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun editModeCase(data: Place?) = (binding as FragmentPlaceEditModeBinding).apply {
        if (data == null) return@apply
        textTitle.setText(data.title)
        textUpdateDate.text = data.updateTime
        textContent.setText(data.content)

        photoList = ArrayList(data.photos?.toList() ?: arrayListOf())

        photoListAdapter.updateData(photoList)

        listPhoto.apply {
            adapter = photoListAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        btnAction.text = requireActivity().getString(R.string.text_button_update)
        btnAction.setOnClickListener {
            val realmList = RealmList<String>().apply { addAll(photoList) }
            val tempData = Place().apply {
                id = dataId
                title = textTitle.text.toString()
                content = textContent.text.toString()
                timestamp = System.currentTimeMillis()
                photos = realmList
            }
            viewModel.updateData(tempData)
            findNavController().navigateUp()
        }
    }

    private fun addModeCase() = (binding as FragmentPlaceEditModeBinding).apply {
        btnAction.text = requireActivity().getString(R.string.text_button_add)
        textUpdateDate.text = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(System.currentTimeMillis())

        listPhoto.apply {
            adapter = photoListAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }

        btnAction.setOnClickListener {
            photoList = photoListAdapter.getPhotoList()
            val realmList = RealmList<String>().apply { addAll(photoList) }
            val tempData = Place().apply {
                title = textTitle.text.toString()
                content = textContent.text.toString()
                timestamp = System.currentTimeMillis()
                photos = realmList
            }
            viewModel.addData(tempData)
            findNavController().navigateUp()
        }
    }

    companion object {
        const val VIEW_MODE = 0
        const val EDIT_MODE = 1
        const val ADD_MODE = 2
    }
}