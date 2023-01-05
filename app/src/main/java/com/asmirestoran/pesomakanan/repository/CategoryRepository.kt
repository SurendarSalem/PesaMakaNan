package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.*
import com.asmirestoran.pesomakanan.model.Category

object CategoryRepository {
    val addCategoryResult: MutableLiveData<Resource<Category>> =
        MutableLiveData<Resource<Category>>()
    val categoryListResult: MutableLiveData<Resource<List<Category>>> =
        MutableLiveData<Resource<List<Category>>>()
    private var categories: List<Category>? = null

    fun addCategory(category: Category) {
        addCategoryResult.value = Resource.loading(null)
        FirebaseHelper.addCategory(
            category, {
                addCategoryResult.value = Resource.success(category)
            }, { _ex ->
                addCategoryResult.value = Resource.error(_ex.message, null)
            })
    }


    fun getAllCategories(fresh: Boolean = false) {
        categoryListResult.value = Resource.loading(null)
        if (fresh || categories.isNullOrEmpty()) {
            FirebaseHelper.getAllCategories(object : CategoryListener {
                override fun onLoad(categories: List<Category>) {
                    categoryListResult.value = Resource.success(categories)
                }

                override fun onError(error: String) {
                    categoryListResult.value = Resource.error(error, null)
                }
            })
        } else {
            categoryListResult.value = Resource.success(categories)
        }
    }
}