package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.OrderRepository

class OrderViewModel : ViewModel() {
    val order = MutableLiveData<Order>()
    val error = MutableLiveData<String>()
    private val orderRepository = OrderRepository()
    val createOrderResult = orderRepository.addOrderResult
    val orderTypes = orderRepository.getOrderTypes()

    init {
        order.value = Order()
    }

    fun addOrder(order: Order) {
        orderRepository.addOrder(order)
    }

    fun createId() {
        order.value?.orderId = Order.createId()
    }

    fun refreshTheTotal() {
        order.value?.let { _order ->
            _order.cartItems?.let {
                _order.totalAmount = 0f
                it.forEach {
                    _order.totalAmount += it.subAmount
                }
            }
            order.value = _order
        }
    }

    fun getTables(): List<String> {
        val list = mutableListOf<String>()
        list.add("Select a table")
        for (i in 1..15) {
            list.add("Table $i")
        }
        return list
    }

    fun setTable(position: Int) {
        order.value?.tableNo = position
    }
}