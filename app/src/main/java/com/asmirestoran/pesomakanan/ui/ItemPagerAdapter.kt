package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.asmirestoran.pesomakanan.ItemListFragment
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Order


class ItemPagerAdapter(
    val fragmentActivity: FragmentActivity,
    val categories: List<Category>,
    val foodItemSelectListener: HomeFragment.OnFoodItemSelectListener,
    val order:Order?
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        return ItemListFragment().apply {
            arguments = Bundle().apply {
                putParcelable("category", categories[position])
                putParcelable("order", order)
            }
            onFoodItemSelectListener = foodItemSelectListener
        }
    }
}