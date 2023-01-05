package com.asmirestoran.pesomakanan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    var code: String,
    var name: String,
    var desc: String,
    var taxPercentage: Float
) : Parcelable {
    constructor() : this("", "", "", 0f)

    companion object {

        fun valid(category: Category): String {
            if (category.code.isEmpty()) {
                return "Please enter code"
            }
            if (category.name.isEmpty()) {
                return "Please enter name"
            }
            if (category.desc.isEmpty()) {
                return "Please enter desc"
            }
            if (category.taxPercentage == 0.0f) {
                return "Please enter tax above 0.0"
            }
            return ""
        }
    }
}