package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentHomeBinding
import com.asmirestoran.pesomakanan.model.CartItem
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Item
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.viewmodel.OrderViewModel
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var orderViewModel: OrderViewModel
    private var order: Order? = null


    override fun getProgressBar() = binding.pb.root

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            order = it.getParcelable(KEY_ORDER)
        }
        orderViewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {

    }

    private fun createItemListAdapter(categories: List<Category>): ItemPagerAdapter {
        return ItemPagerAdapter(requireActivity(), categories, object : OnFoodItemSelectListener {
            override fun onFoodSelected(selectedFoodItem: Item) {
                addOrUpdateOrder(selectedFoodItem)
            }
        })
    }

    private fun addOrUpdateOrder(selectedFoodItem: Item) {
        order?.cartItems?.let {
            if (cartItemsContains(it, selectedFoodItem)) {
            } else {
                it.add((CartItem().apply {
                    item = selectedFoodItem
                    quantity = 1
                    subAmount = quantity * selectedFoodItem.price
                }))
            }
        } ?: kotlin.run {
            order?.cartItems = mutableListOf((CartItem().apply {
                item = selectedFoodItem
                quantity = 1
                subAmount = quantity * selectedFoodItem.price
            }))
        }

        order?.let { _order ->
            _order.totalAmount = 0f
            _order.cartItems?.forEach {
                _order.totalAmount += it.subAmount
            }
        }

        orderViewModel.order.value = order
        Log.d("Surendar", order.toString())
    }

    private fun cartItemsContains(it: MutableList<CartItem>, selectedFoodItem: Item): Boolean {
        it.forEach {
            if (it.item?.equals(selectedFoodItem) == true) {
                it.quantity++
                it.subAmount = it.quantity * selectedFoodItem.price
                return true
            }
        }
        return false
    }

    private fun observe() {
        homeViewModel.getAllCategories()
        homeViewModel.categoryListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    resource.data?.let { categories ->
                        binding.viewPager.offscreenPageLimit = 1
                        binding.viewPager.adapter = createItemListAdapter(categories)
                        TabLayoutMediator(
                            binding.tabs, binding.viewPager
                        ) { tab, position -> tab.text = categories[position].name }.attach()

                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), resource.message)
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {

    }

    interface OnFoodItemSelectListener {
        fun onFoodSelected(selectedFoodItem: Item)
    }
}