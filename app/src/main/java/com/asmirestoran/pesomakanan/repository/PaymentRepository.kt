package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.EmployeeDetailsCallback
import com.asmirestoran.pesomakanan.FirebaseHelper
import com.asmirestoran.pesomakanan.Resource
import com.asmirestoran.pesomakanan.model.Employee
import com.asmirestoran.pesomakanan.model.Payment

class PaymentRepository {

    val addPaymentResult: MutableLiveData<Resource<Payment>> =
        MutableLiveData<Resource<Payment>>()

    val employeeDetailResult: MutableLiveData<Resource<Employee>> =
        MutableLiveData<Resource<Employee>>()

    val employeeUpdateResult: MutableLiveData<Resource<Employee>> =
        MutableLiveData<Resource<Employee>>()

    fun addPayment(item: Payment) {
        addPaymentResult.value = Resource.loading(null)
        FirebaseHelper.addPayment(
            item, {
                addPaymentResult.value = Resource.success(item)
            }, { _ex ->
                addPaymentResult.value = Resource.error(_ex.message, null)
            })
    }

    fun getEmployeeDetail(id: String) {
        employeeDetailResult.value = Resource.loading(null)
        FirebaseHelper.getEmployee(id, object : EmployeeDetailsCallback {
            override fun onDetailsLoaded(employee: Employee) {
                employeeDetailResult.value = Resource.success(employee)
            }

            override fun onLoadFailed(error: String) {
                employeeDetailResult.value = Resource.error(error, null)
            }
        })
    }

    fun updateEmployee(employee: Employee) {
        employeeUpdateResult.value = Resource.loading(null)
        FirebaseHelper.addOrUpdateEmployee(employee, {
            employeeUpdateResult.value = Resource.success(employee)
        }, {
            employeeUpdateResult.value = Resource.error(it.message, null)
        })
    }
}