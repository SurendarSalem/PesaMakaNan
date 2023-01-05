package com.asmirestoran.pesomakanan

import com.asmirestoran.pesomakanan.model.*

interface CategoryListener {
    fun onLoad(categories: List<Category>)
    fun onError(error: String)
}

interface ItemListListener {
    fun onLoad(categories: List<Item>)
    fun onError(error: String)
}

interface PurchaseListListener {
    fun onLoad(purchases: List<Purchase>)
    fun onError(error: String)
}

interface AttendanceListener {
    fun onLoad(attendances: List<Attendance>)
    fun onError(error: String)
}

interface PaymentListener {
    fun onLoad(payments: List<Payment>)
    fun onError(error: String)
}
interface OrderListener {
    fun onLoad(orders: List<Order>)
    fun onError(error: String)
}