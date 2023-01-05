package com.asmirestoran.pesomakanan.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Item
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.ItemRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class AddItemViewModel : ViewModel() {
    val item = MutableLiveData<Item>()
    val error = MutableLiveData<String>()
    val itemRepository = ItemRepository()
    val categoryListResult = itemRepository.itemListResult
    val addItemResult = itemRepository.addItemResult
    val uploadImageResult = itemRepository.uploadImageResult

    init {
        item.value = Item()
        item.value?.let {
            it.code = Item.createCode()
        }
    }

    fun addItem() {
        item.value?.let {
            itemRepository.addItem(it)
        }
    }

    fun uploadImage(bitmap: Bitmap) {
        item.value?.let {
            itemRepository.uploadImage(bitmap, it)
        }
    }
}