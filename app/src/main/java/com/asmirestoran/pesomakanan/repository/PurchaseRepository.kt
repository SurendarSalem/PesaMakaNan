package com.asmirestoran.pesomakanan.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.*
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Purchase
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class PurchaseRepository {
    val addPurchaseResult: MutableLiveData<Resource<Purchase>> =
        MutableLiveData<Resource<Purchase>>()

    val purchaseListResult: MutableLiveData<Resource<List<Purchase>>> =
        MutableLiveData<Resource<List<Purchase>>>()

    private var purchases: List<Purchase>? = null

    fun addPurchase(item: Purchase) {
        addPurchaseResult.value = Resource.loading(null)
        FirebaseHelper.addPurchase(
            item, {
                addPurchaseResult.value = Resource.success(item)
            }, { _ex ->
                addPurchaseResult.value = Resource.error(_ex.message, null)
            })
    }

    fun getAllPurchases(fresh: Boolean = false) {
        purchaseListResult.value = Resource.loading(null)
        if (fresh || purchases.isNullOrEmpty()) {
            FirebaseHelper.getAllPurchases(object : PurchaseListListener {
                override fun onLoad(purchases: List<Purchase>) {
                    purchaseListResult.value = Resource.success(purchases)
                }

                override fun onError(error: String) {
                    purchaseListResult.value = Resource.error(error, null)
                }
            })
        } else {
            purchaseListResult.value = Resource.success(purchases)
        }
    }
}