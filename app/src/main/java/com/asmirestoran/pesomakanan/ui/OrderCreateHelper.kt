package com.asmirestoran.pesomakanan.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import com.asmirestoran.pesomakanan.databinding.LayoutOrderCreateBinding
import com.asmirestoran.pesomakanan.model.Order
import com.google.android.material.bottomsheet.BottomSheetDialog

class OrderCreateHelper(
    val context: Context,
    private val orderListener: OrderListener,
    val layoutInflater: LayoutInflater
) {

    var order = Order()

    fun showOrderDialog() {
        val dialog = BottomSheetDialog(context)
        dialog.setTitle("Create Order")
        val binding = LayoutOrderCreateBinding.inflate(layoutInflater)
        binding.btnRegister.setOnClickListener {
            dialog.dismiss()
            orderListener.onOrderCreated(order)
        }
        binding.order = order
        dialog.setContentView(binding.root)
        dialog.show()
    }

    interface OrderListener {
        fun onOrderCreated(order: Order)
    }
}