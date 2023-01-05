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
import com.asmirestoran.pesomakanan.databinding.LayoutOrderListBinding
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.ui.adapter.OrderListAdapter
import com.asmirestoran.pesomakanan.viewmodel.OrderListViewModel
import com.asmirestoran.pesomakanan.viewmodel.OrderViewModel

class OrderListFragment : BaseFragment() {
    private lateinit var binding: LayoutOrderListBinding
    private val orderListViewModel: OrderListViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    lateinit var adapter: OrderListAdapter
    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_order_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun observe() {
        orderListViewModel.orderListResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    it.data?.let {
                        adapter.updateData(it)
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), it.message)
                }
            }
        }
        orderViewModel.createOrderResult.observe(requireActivity()) {
            if (it.status == Status.SUCCESS) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initViews() {
        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrderListAdapter(requireContext(), orderClickListener)
        binding.rvOrders.adapter = adapter
        binding.btnCreateOrder.setOnClickListener {
            (requireActivity() as OrderActivity).openCreateOrder()
        }
    }

    private val orderClickListener = object : OrderListAdapter.OrderClickListener {
        override fun onOrderClicked(order: Order) {
            (requireActivity() as OrderActivity).openCreateOrder(order)
        }
    }
}