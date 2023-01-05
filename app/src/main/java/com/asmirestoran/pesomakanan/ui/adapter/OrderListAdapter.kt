package com.asmirestoran.pesomakanan.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.databinding.LayoutOrderItemBinding

class OrderListAdapter(val context: Context, val orderClickListener: OrderClickListener) :
    RecyclerView.Adapter<OrderListAdapter.OrderHolder>() {

    var orders: List<Order> = mutableListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(
            context,
            LayoutOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(orders[position], orderClickListener)
    }

    override fun getItemCount() = orders.size

    fun updateData(orders: List<Order>) {
        this.orders = orders
        notifyItemRangeChanged(0, orders.size - 1)
    }

    class OrderHolder(val context: Context, val binding: LayoutOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, orderClickListener: OrderClickListener) {
            binding.order = order
            binding.root.setOnClickListener {
                orderClickListener.onOrderClicked(order)
            }
        }
    }

    interface OrderClickListener {
        fun onOrderClicked(order: Order)
    }
}