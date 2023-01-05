package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.repository.CategoryRepository

class CategoryViewModel : ViewModel() {
    val category = MutableLiveData<Category>()
    val error = MutableLiveData<String>()
    val categoryListResult = CategoryRepository.categoryListResult
    val addCategoryResult = CategoryRepository.addCategoryResult

    init {
        category.value = Category("", "", "", 0f)
    }

    fun addCategory(category: Category) {
        CategoryRepository.addCategory(category)
    }

    fun getAllCategories() {
        CategoryRepository.getAllCategories()
    }
}