package com.asmirestoran.pesomakanan.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.databinding.LayoutCartItemBinding
import com.asmirestoran.pesomakanan.model.CartItem

class CartItemsAdapter(val context: Context, val orderEditListener: OrderEditListener) :
    RecyclerView.Adapter<CartItemsAdapter.CartItemHolder>() {

    var cartItems: ArrayList<CartItem> = ArrayList()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemHolder {
        return CartItemHolder(
            context,
            LayoutCartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartItemHolder, position: Int) {
        holder.bind(cartItems[position], orderEditListener)
    }

    override fun getItemCount() = cartItems.size

    fun updateData(cartItems: ArrayList<CartItem>) {
        this.cartItems = cartItems
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int) {
        this.cartItems.removeAt(pos)
        notifyItemRemoved(pos)
    }

    class CartItemHolder(val context: Context, val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem, orderEditListener: OrderEditListener) {
            binding.cartItem = cartItem
            binding.btnPlus.setOnClickListener {
                binding.tvItemQty.text = (cartItem.quantity + 1).toString()
                cartItem.item?.let {
                    binding.tvSubAmount.text = (cartItem.quantity * it.price).toString()
                }
                orderEditListener.onOrderEdited()
            }
            binding.btnMinus.setOnClickListener {
                if (cartItem.quantity <= 0) {
                    binding.tvItemQty.text = 0.toString()
                    orderEditListener.onItemRemoved(bindingAdapterPosition)
                } else {
                    binding.tvItemQty.text = (cartItem.quantity - 1).toString()
                    cartItem.item?.let {
                        binding.tvSubAmount.text = (cartItem.quantity * it.price).toString()
                    }
                }
                orderEditListener.onOrderEdited()
            }
        }
    }

    interface OrderEditListener {
        fun onOrderEdited()
        fun onItemRemoved(pos: Int)
    }
}