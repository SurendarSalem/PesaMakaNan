package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.FirebaseHelper
import com.asmirestoran.pesomakanan.OrderListener
import com.asmirestoran.pesomakanan.PurchaseListListener
import com.asmirestoran.pesomakanan.Resource
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.model.OrderType
import com.asmirestoran.pesomakanan.model.Purchase

class OrderRepository {

    // private var orders: MutableList<Order> = mutableListOf()
    val addOrderResult: MutableLiveData<Resource<Order>> =
        MutableLiveData<Resource<Order>>()
    val orderListResult: MutableLiveData<Resource<List<Order>>> =
        MutableLiveData<Resource<List<Order>>>()

    fun addOrder(order: Order) {
        addOrderResult.value = Resource.loading(order)
        FirebaseHelper.addOrder(
            order, {
                addOrderResult.value = Resource.success(order)
            }, { _ex ->
                addOrderResult.value = Resource.error(_ex.message, null)
            })
    }

    fun getOrderTypes(): List<OrderType> {
        return listOf(OrderType.NONE, OrderType.TABLE, OrderType.COUNTER, OrderType.ONLINE)
    }

    fun getAllOrders(fresh: Boolean = false) {
        orderListResult.value = Resource.loading(null)
        FirebaseHelper.getOrders(object : OrderListener {
            override fun onLoad(orders: List<Order>) {
                orderListResult.value = Resource.success(orders)
            }

            override fun onError(error: String) {
                orderListResult.value = Resource.error(error, null)
            }
        })
    }
}