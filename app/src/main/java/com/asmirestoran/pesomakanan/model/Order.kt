package com.asmirestoran.pesomakanan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Order() : Parcelable {
    var orderId: String = ""
    var orderName: String = ""
    var cartItems: ArrayList<CartItem>? = null
    var taxAmount: Float = 0f
    var totalAmount: Float = 0f
    var orderType: OrderType? = null
    var tableNo: Int = 0

    companion object {
        fun createId(): String {
            return "OR${System.currentTimeMillis()}"
        }

        fun valid(order: Order): String {
            if (order.orderId.isEmpty()) {
                return "Please reopen screen to create order id"
            }
            if (order.orderName.isEmpty()) {
                return "Please enter order name"
            }
            if (order.orderType == null || order.orderType == OrderType.NONE) {
                return "Please select order type"
            }
            if (order.orderType == OrderType.TABLE) {
                if (order.tableNo == 0) {
                    return "Please select Table Number"
                }
            }
            return ""
        }
    }

    override fun toString(): String {
        return "Order(orderId='$orderId', orderName='$orderName', cartItems=$cartItems, taxAmount=$taxAmount, totalAmount=$totalAmount, orderType=$orderType)"
    }


}

enum class OrderType(val type: String) {
    NONE("Select order type"),
    TABLE("Dine In"),
    COUNTER("Counter"),
    ONLINE("Online");

    override fun toString(): String {
        return "OrderType(type='$type')"
    }


}