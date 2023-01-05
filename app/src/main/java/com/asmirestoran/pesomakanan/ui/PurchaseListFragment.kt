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
import com.asmirestoran.pesomakanan.databinding.LayoutPurchaseListBinding
import com.asmirestoran.pesomakanan.ui.adapter.PurchaseListAdapter
import com.asmirestoran.pesomakanan.viewmodel.PurchaseListViewModel

class PurchaseListFragment : BaseFragment() {
    private lateinit var binding: LayoutPurchaseListBinding
    private val purchaseListViewModel: PurchaseListViewModel by viewModels()
    private lateinit var purchaseListAdapter: PurchaseListAdapter

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_purchase_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {
        binding.rvPurchases.layoutManager = LinearLayoutManager(requireContext())
        purchaseListAdapter = PurchaseListAdapter(requireContext())
        binding.rvPurchases.adapter = purchaseListAdapter
        binding.btnAddPurchase.setOnClickListener {
            (requireActivity() as OrderActivity).openAddPurchaseScreen()
        }
    }

    private fun observe() {
        purchaseListViewModel.getAllPurchases()
        purchaseListViewModel.purchaseListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    resource.data?.let { purchases ->
                        purchaseListAdapter.updateData(purchases)
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