package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentCreateOrderBinding
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.model.OrderType
import com.asmirestoran.pesomakanan.ui.adapter.CartItemsAdapter
import com.asmirestoran.pesomakanan.ui.adapter.OrderTypeSpinnerAdapter
import com.asmirestoran.pesomakanan.ui.adapter.TableAdapter
import com.asmirestoran.pesomakanan.viewmodel.OrderViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

const val KEY_ORDER = "order"

class OrderFragment : BaseFragment() {
    lateinit var binding: FragmentCreateOrderBinding
    private lateinit var orderViewModel: OrderViewModel
    var error: String = ""
    lateinit var cartItemsAdapter: CartItemsAdapter
    private var orderFromEdit: Order? = null
    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderViewModel = ViewModelProvider(requireActivity()).get(OrderViewModel::class.java)
        getDataFromBundle(arguments)
    }

    private fun getDataFromBundle(arguments: Bundle?) {
        arguments?.let {
            orderFromEdit = it.getParcelable(KEY_ORDER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_order, container, false)
        initViews()
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderViewModel = orderViewModel

    }

    private fun observe() {
        orderViewModel.createOrderResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Created order ${it.data?.orderName}"
                    )
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), it.message)
                }
            }
        }
        orderViewModel.order.observe(viewLifecycleOwner) { order ->
            order.cartItems?.let {
                if (it.isNotEmpty()) {
                    cartItemsAdapter.updateData(it)
                    binding.llTotalAmount.visibility = View.VISIBLE
                    binding.tvTotalAmount.text = order.totalAmount.toString()
                }
            }
        }
    }

    private fun initViews() {
        setDataForSpinner()
        binding.btnAddOrder.setOnClickListener {
            orderViewModel.createId()
            orderViewModel.order.value?.let { order ->
                val error = Order.valid(order)
                binding.error = error
                if (error.isEmpty()) {
                    orderViewModel.addOrder(order)
                }
            }
        }
        binding.btnUpdateOrder.setOnClickListener {
            orderViewModel.order.value?.let { order ->
                val error = Order.valid(order)
                binding.error = error
                if (error.isEmpty()) {
                    orderViewModel.addOrder(order)
                }
            }
        }
        orderFromEdit?.let {
            orderViewModel.order.value = orderFromEdit
            binding.btnAddOrder.visibility = View.GONE
            binding.btnUpdateOrder.visibility = View.VISIBLE
        } ?: kotlin.run {
            binding.btnAddOrder.visibility = View.VISIBLE
            binding.btnUpdateOrder.visibility = View.GONE
        }
        binding.btnAddItem.setOnClickListener {
            (requireActivity() as OrderActivity).openCatalogScreen(orderViewModel.order.value)
        }
        cartItemsAdapter = CartItemsAdapter(requireContext(), orderEditListener)
        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = cartItemsAdapter
    }

    val orderEditListener = object : CartItemsAdapter.OrderEditListener {
        override fun onOrderEdited() {
            orderViewModel.refreshTheTotal()
        }

        override fun onItemRemoved(pos: Int) {
            cartItemsAdapter.removeItem(pos)
        }
    }

    private fun setDataForSpinner() {
        val adapter = OrderTypeSpinnerAdapter(requireContext(), orderViewModel.orderTypes)
        binding.spOrderType.adapter = adapter
        binding.spOrderType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    orderViewModel.order.value?.orderType = adapter.getItem(position)
                    if (adapter.getItem(position) == OrderType.TABLE) {
                        binding.spTableNumber.visibility = View.VISIBLE
                    } else {
                        binding.spTableNumber.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        val tableAdapter = TableAdapter(
            requireContext(),
            getTables()
        )
        binding.spTableNumber.adapter = tableAdapter
        binding.spTableNumber.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    orderViewModel.setTable(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        orderFromEdit?.let {
            binding.spOrderType.setSelection(orderViewModel.orderTypes.indexOf(it.orderType))
            binding.spTableNumber.setSelection(it.tableNo)
        }
    }

    private fun getTables(): List<String> {
        return orderViewModel.getTables()
    }
}