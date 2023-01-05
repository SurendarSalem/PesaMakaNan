package com.asmirestoran.pesomakanan.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Item
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.ItemRepository

class ItemListViewModel : ViewModel() {

    var itemRepository: ItemRepository = ItemRepository();
    val error = MutableLiveData<String>()
    val itemListResult = itemRepository.itemListResult

    fun getItems(category: Category, fresh: Boolean) {
        itemRepository.getItems(category, fresh)
    }
}