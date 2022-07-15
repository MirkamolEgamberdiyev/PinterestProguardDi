package com.mirkamol.proguardpinterest.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mirkamol.proguardpinterest.R
import com.mirkamol.proguardpinterest.adapters.HomePhotoAdapter
import com.mirkamol.proguardpinterest.databinding.FragmentHomeBinding
import com.mirkamol.proguardpinterest.extantions.viewBinding
import com.mirkamol.proguardpinterest.model.HomePhotoItem
import com.mirkamol.proguardpinterest.utils.UiStateList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewmodel by viewModels<HomeViewModel>()
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val adapter by lazy { HomePhotoAdapter() }
    private var PAGE = 1
    private var PER_PAGE = 15
    private val photos = ArrayList<HomePhotoItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.getAllPhotos(PAGE, PER_PAGE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()

    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.getPhotosState.collect {
                    when (it) {
                        is UiStateList.LOADING -> {

                        }
                        is UiStateList.SUCCESS -> {
                            photos.addAll(photos.size,it.data)
                            Log.d("@@@", "setupObservers: ${it.data}")
                            adapter.submitList(photos.toList())
                        }
                        is UiStateList.ERROR -> {
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            rvHomePhotos.adapter = adapter
            nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (nestedScroll.getChildAt(nestedScroll.childCount - 1) != null) {
                    if (scrollY >= nestedScroll.getChildAt(nestedScroll.childCount - 1).measuredHeight - nestedScroll.measuredHeight &&
                        scrollY > oldScrollY
                    ) {
                        PAGE++
                        Log.d("page", "setupUI: $PAGE")
                        viewmodel.getAllPhotos(PAGE, PER_PAGE)
                        //Toast.makeText(requireContext(), "the end", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }


    }


}