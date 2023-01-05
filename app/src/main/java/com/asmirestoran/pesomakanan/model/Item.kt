package com.asmirestoran.pesomakanan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var code: String = "",
    var name: String = "",
    var desc: String = "",
    var price: Float = 0f,
    var category: Category? = null,
    var imageURL: String = ""
) : Parcelable {

    constructor() : this("", "", "", 0f, null, "")

    companion object {

        fun valid(item: Item?): String {
            if (item == null) {
                return "Please enter code"
            }
            if (item.code.isEmpty()) {
                return "Please enter code"
            }
            if (item.name.isEmpty()) {
                return "Please enter name"
            }
            if (item.desc.isEmpty()) {
                return "Please enter desc"
            }
            if (item.price == 0.0f) {
                return "Please enter tax above 0.0"
            }
            if (item.category == null) {
                return "Please enter tax above 0.0"
            }
            if (item.imageURL.isEmpty()) {
                return "Please select image"
            }
            return ""
        }

        fun createCode(): String {
            return "IT${System.currentTimeMillis()}"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }


}