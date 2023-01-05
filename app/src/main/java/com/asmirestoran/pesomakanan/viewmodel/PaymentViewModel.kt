package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Employee
import com.asmirestoran.pesomakanan.model.Payment
import com.asmirestoran.pesomakanan.repository.PaymentRepository
import com.asmirestoran.pesomakanan.ui.Utils

class PaymentViewModel : ViewModel() {
    val paymentLiveData = MutableLiveData<Payment>()

    val error = MutableLiveData<String>()
    private val paymentRepository = PaymentRepository()
    val addPaymentResult = paymentRepository.addPaymentResult
    val employeeDetailResult = paymentRepository.employeeDetailResult
    val employeeUpdateResult = paymentRepository.employeeUpdateResult

    init {
        initData()
    }

    private fun initData() {
        paymentLiveData.value = Payment().apply {
            paymentId = Payment.createId()
            date = Utils.getDate()
        }
    }

    fun addPayment() {
        paymentLiveData.value?.let {
            paymentRepository.addPayment(it)
        }
    }

    fun reset() {
        initData()
    }

    fun getEmployeeDetail(id: String) {
        paymentRepository.getEmployeeDetail(id)
    }

    fun updateEmployee(employee: Employee) {
        paymentRepository.updateEmployee(employee)
    }

    fun getEmployee(): Employee? {
        return employeeDetailResult.value?.data
    }
}