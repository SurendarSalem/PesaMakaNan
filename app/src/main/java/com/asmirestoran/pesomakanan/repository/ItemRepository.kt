package com.asmirestoran.pesomakanan.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.*
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.model.Item
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class ItemRepository {
    val addItemResult: MutableLiveData<Resource<Item>> =
        MutableLiveData<Resource<Item>>()
    val itemListResult: MutableLiveData<Resource<List<Item>>> =
        MutableLiveData<Resource<List<Item>>>()
    val uploadImageResult: MutableLiveData<Resource<String>> =
        MutableLiveData<Resource<String>>()

    fun addItem(item: Item) {
        addItemResult.value = Resource.loading(null)
        FirebaseHelper.addItem(
            item, {
                addItemResult.value = Resource.success(item)
            }, { _ex ->
                addItemResult.value = Resource.error(_ex.message, null)
            })
    }

    fun uploadImage(
        bitmap: Bitmap,
        item: Item
    ) {
        uploadImageResult.value = Resource.loading(null)
        FirebaseHelper.uploadImage(bitmap, {
            uploadImageResult.value = Resource.success(it)
        }, {
            uploadImageResult.value = Resource.error(it.message, null)
        }, item)
    }

    fun getItems(category: Category, fresh: Boolean) {
        itemListResult.value = Resource.loading(null)
        if (fresh) {
            FirebaseHelper.getItems(category, object : ItemListListener {
                override fun onLoad(categories: List<Item>) {
                    itemListResult.value = Resource.success(categories)
                }

                override fun onError(error: String) {
                    CategoryRepository.categoryListResult.value = Resource.error(error, null)
                }
            })
        }
    }
}