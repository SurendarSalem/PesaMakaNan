package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.OrderRepository

class OrderListViewModel : ViewModel() {
    private val orderRepository = OrderRepository()
    val orderListResult = orderRepository.orderListResult

    init {
        getAllOrders()
    }

    fun getAllOrders() {
        orderRepository.getAllOrders()
    }
}