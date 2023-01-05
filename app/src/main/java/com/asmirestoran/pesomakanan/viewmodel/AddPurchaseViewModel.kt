package com.asmirestoran.pesomakanan.viewmodel

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.databinding.ObservableFloat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.CustomPayment
import com.asmirestoran.pesomakanan.model.Purchase
import com.asmirestoran.pesomakanan.repository.PurchaseRepository

class AddPurchaseViewModel : ViewModel() {
    val purchaseLiveData = MutableLiveData<Purchase>().apply {
        value = Purchase().apply { purchaseId = Purchase.createId() }
    }

    val error = MutableLiveData<String>()
    private val purchaseRepository = PurchaseRepository()
    val addPurchaseResult = purchaseRepository.addPurchaseResult

    fun addPurchase() {
        purchaseLiveData.value?.let {
            purchaseRepository.addPurchase(it)
        }
    }
}