package com.asmirestoran.pesomakanan.ui

import android.graphics.Bitmap
import com.asmirestoran.pesomakanan.model.Item
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class FireBaseUploader(
    private val bitmap: Bitmap,
    private val successListener: OnSuccessListener<String>,
    private val failureListener: OnFailureListener,
    val item: Item
) {
    private val IMAGES_FOLDER_NAME: String = "item_images"
    lateinit var storage: FirebaseStorage

    fun uploadImageToFirebase(item: Item) {
        storage = Firebase.storage
        val storageRef = storage.reference
        val itemRef = storageRef.child(IMAGES_FOLDER_NAME).child(item.code + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = itemRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            itemRef.downloadUrl.addOnSuccessListener {
                successListener.onSuccess(it.toString())
            }
        }.addOnFailureListener(failureListener)
    }
}