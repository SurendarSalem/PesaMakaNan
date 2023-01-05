package com.asmirestoran.pesomakanan.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.databinding.LayoutListItemBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    var categories: List<Category> = mutableListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            LayoutListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
    fun updateData(categories: List<Category>) {
        this.categories = categories
        notifyItemRangeChanged(0, categories.size - 1)
    }

    class CategoryHolder(val binding: LayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.title = category.name
        }
    }
}