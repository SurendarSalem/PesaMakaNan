package com.asmirestoran.pesomakanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.asmirestoran.pesomakanan.databinding.LayoutItemListBinding
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.ui.Utils
import com.asmirestoran.pesomakanan.ui.adapter.ItemListAdapter
import com.asmirestoran.pesomakanan.ui.BaseFragment
import com.asmirestoran.pesomakanan.ui.HomeFragment
import com.asmirestoran.pesomakanan.ui.ItemListViewModel

import com.asmirestoran.pesomakanan.ui.adapter.ItemOffsetDecoration


class ItemListFragment : BaseFragment() {
    private lateinit var binding: LayoutItemListBinding
    private val itemListViewModel: ItemListViewModel by viewModels()
    private lateinit var itemListAdapter: ItemListAdapter
    var category: Category? = null
    var onFoodItemSelectListener: HomeFragment.OnFoodItemSelectListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable("category")
        }
    }

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_item_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {
        binding.rvItems.layoutManager = GridLayoutManager(context, 3);
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset);
        binding.rvItems.addItemDecoration(itemDecoration);
        itemListAdapter = ItemListAdapter(onFoodItemSelectListener)
        binding.rvItems.adapter = itemListAdapter
    }

    private fun observe() {
        category?.let { itemListViewModel.getItems(it, true) }
        itemListViewModel.itemListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    resource.data?.let { items ->
                        kotlin.run {
                            itemListAdapter.updateData(items)
                        }
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