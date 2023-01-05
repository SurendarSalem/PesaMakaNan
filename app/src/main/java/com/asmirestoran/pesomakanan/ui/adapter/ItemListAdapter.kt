package com.asmirestoran.pesomakanan.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.databinding.LayoutFoodItemBinding
import com.asmirestoran.pesomakanan.model.Item
import com.asmirestoran.pesomakanan.ui.HomeFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ItemListAdapter(val onFoodItemSelectListener: HomeFragment.OnFoodItemSelectListener?) :
    RecyclerView.Adapter<ItemListAdapter.ItemHolder>() {

    var items: List<Item> = mutableListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutFoodItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position], onFoodItemSelectListener)
    }

    override fun getItemCount() = this.items.size
    fun updateData(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemHolder(val binding: LayoutFoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item, onFoodItemSelectListener: HomeFragment.OnFoodItemSelectListener?) {
            binding.title = item.name
            Glide.with(binding.root.context)
                .load(item.imageURL)
                .override(binding.ivIcon.width, binding.ivIcon.height)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivIcon)
            binding.root.setOnClickListener {
                onFoodItemSelectListener?.onFoodSelected(item)
            }
        }
    }
}