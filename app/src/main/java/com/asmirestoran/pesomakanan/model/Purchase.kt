package com.asmirestoran.pesomakanan.model

import android.os.Parcelable
import android.util.Patterns
import com.asmirestoran.pesomakanan.User
import kotlinx.android.parcel.Parcelize

@Parcelize
class Purchase : Parcelable {

    var purchaseId: String = ""
    var amount: Float = 0f
    var vendorName: String = ""
    var taxPercentage: Float = 0f
        get() {
            return "%.1f".format(field).toFloat()
        }
    var taxAmount: Float = 0f
        get() {
            return "%.1f".format(field).toFloat()
        }
    var totalAmount: Float = 0f
        get() {
            return "%.1f".format(field).toFloat()
        }
    var orderDate: String = ""
    var paymentMethod: PaymentMethod? = null
    var paidAmount: Float = 0f
    var balanceAmount: Float = 0f
    var netPayment: NetPayment? = null
    var customPayment: CustomPayment? = null
    var emiPayment: EmiPayment? = null

    companion object {
        fun createId(): String {
            return "PR${System.currentTimeMillis()}"
        }

        fun valid(purchase: Purchase): String {
            if (purchase.vendorName.isEmpty()) {
                return "Please enter Vendor name"
            }
            if (purchase.amount <= 0) {
                return "Please enter amount"
            }
            if (purchase.taxAmount > 0.0) {
                if (purchase.totalAmount <= 0) {
                    return "Please enter total amount"
                }
            }
            if (purchase.orderDate.isEmpty()) {
                return "Please enter order date"
            }
            if (purchase.paymentMethod == null) {
                return "Please select a payment method"
            } else {
                when (purchase.paymentMethod) {
                    PaymentMethod.NET -> {
                        if (purchase.amount <= 0) {
                            return "Please enter amount"
                        }
                    }
                    PaymentMethod.CUSTOM -> {
                        purchase.customPayment?.let {
                            if (purchase.balanceAmount > 0 && it.pendingPaymentDate.isEmpty()) {
                                return "Please enter pending payment date"
                            }
                        } ?: run {
                            return "Please enter custom payment details"
                        }
                    }
                    PaymentMethod.EMI -> {
                        purchase.emiPayment?.let {
                            if (it.emiAmount <= 0) {
                                return "Please enter EMI amount"
                            }
                            if (it.emiDuration <= 0) {
                                return "Please enter EMI duration"
                            }
                            if (it.startDate.isEmpty()) {
                                return "Please enter EMI start date"
                            }
                            if (it.startDate.isEmpty()) {
                                return "Please enter EMI end date"
                            }
                        } ?: run {
                            return "Please enter EMI payment details"
                        }
                    }
                    else -> {}
                }
            }
            if (purchase.paidAmount <= 0) {
                return "Please enter paid amount"
            }
            if (purchase.totalAmount != purchase.paidAmount && purchase.balanceAmount <= 0) {
                return "Please enter balance amount"
            }
            return ""
        }
    }
}


enum class PaymentMethod {
    NET, CUSTOM, EMI
}

@Parcelize
data class NetPayment(var amount: Float = 0f) : Parcelable

@Parcelize
data class CustomPayment(var pendingPaymentDate: String = "") :
    Parcelable

@Parcelize
data class EmiPayment(
    var emiAmount: Float = 0f,
    var emiDuration: Int = 0,
    var startDate: String = "",
    var endDate: String = "",
) : Parcelable
