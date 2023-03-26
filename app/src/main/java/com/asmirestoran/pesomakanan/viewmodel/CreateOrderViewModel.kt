package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.OrderRepository

class CreateOrderViewModel : ViewModel() {
    val order = MutableLiveData<Order>()
    val error = MutableLiveData<String>()
    private val orderRepository = OrderRepository()
    val createOrderResult = orderRepository.addOrderResult

    init {
        order.value = Order()
    }

    fun addOrder(order: Order) {
        orderRepository.addOrder(order)
    }

    fun createId() {
        order.value?.orderId = Order.createId()
    }
}