package com.asmirestoran.pesomakanan.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.repository.CategoryRepository

class HomeViewModel : ViewModel() {
    val categoryListResult = CategoryRepository.categoryListResult

    init {

    }

    fun getAllCategories() {
        CategoryRepository.getAllCategories()
    }
}