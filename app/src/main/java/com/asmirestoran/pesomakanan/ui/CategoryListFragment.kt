package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.LayoutCategoryListBinding
import com.asmirestoran.pesomakanan.ui.adapter.CategoryAdapter
import com.asmirestoran.pesomakanan.viewmodel.CategoryViewModel

class CategoryListFragment : BaseFragment() {
    private lateinit var binding: LayoutCategoryListBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_category_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        categoryAdapter = CategoryAdapter()
        binding.rvCategories.adapter = categoryAdapter
        binding.btnCategoryScreen.setOnClickListener {
            (requireActivity() as OrderActivity).openAddCategoryScreen()
        }
    }

    private fun observe() {
        categoryViewModel.getAllCategories()
        categoryViewModel.categoryListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    resource.data?.let { categories ->
                        categoryAdapter.updateData(categories)
                        Utils.showSnack(requireActivity(), "Categories loaded")
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), resource.message)
                }
            }
        }
    }
}