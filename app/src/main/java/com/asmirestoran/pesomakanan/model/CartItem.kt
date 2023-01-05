package com.asmirestoran.pesomakanan.model

class CartItem {
    var cartId: String = ""
    var item: Item? = null
    var quantity: Int = 0
    var subAmount: Float = 0f

    override fun toString(): String {
        return "CartItem(cartId='$cartId', item=$item, quantity=$quantity, subAmount=$subAmount)"
    }


}