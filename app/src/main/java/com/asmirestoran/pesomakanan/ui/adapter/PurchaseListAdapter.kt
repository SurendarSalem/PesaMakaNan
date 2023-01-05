package com.asmirestoran.pesomakanan.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.model.Purchase
import com.asmirestoran.pesomakanan.databinding.LayoutPurchaseItemBinding
import com.asmirestoran.pesomakanan.model.PaymentMethod

class PurchaseListAdapter(val context: Context) :
    RecyclerView.Adapter<PurchaseListAdapter.PurchaseHolder>() {

    var purchases: List<Purchase> = mutableListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHolder {
        return PurchaseHolder(
            context,
            LayoutPurchaseItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PurchaseHolder, position: Int) {
        holder.bind(purchases[position])
    }

    override fun getItemCount() = purchases.size
    fun updateData(purchases: List<Purchase>) {
        this.purchases = purchases
        notifyItemRangeChanged(0, purchases.size - 1)
    }

    class PurchaseHolder(val context: Context, val binding: LayoutPurchaseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(purchase: Purchase) {
            binding.purchase = purchase
            binding.tvSubAmount.text = context.getString(R.string.Rs) + purchase.totalAmount.toInt()
            when (purchase.paymentMethod) {
                PaymentMethod.NET -> {
                    binding.tvType.text = context.getString(R.string.net_payment)
                }
                PaymentMethod.CUSTOM -> {
                    binding.tvType.text = context.getString(R.string.custom_payment)
                }
                PaymentMethod.EMI -> {
                    binding.tvType.text = context.getString(R.string.emi)
                }
                else -> {}
            }
        }
    }
}