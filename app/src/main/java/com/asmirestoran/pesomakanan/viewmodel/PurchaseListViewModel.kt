package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.repository.CategoryRepository
import com.asmirestoran.pesomakanan.repository.PurchaseRepository

class PurchaseListViewModel : ViewModel() {

    private val purchaseRepository = PurchaseRepository()
    val purchaseListResult = purchaseRepository.purchaseListResult

    fun getAllPurchases(fresh: Boolean = false) {
        purchaseRepository.getAllPurchases(fresh)
    }
}