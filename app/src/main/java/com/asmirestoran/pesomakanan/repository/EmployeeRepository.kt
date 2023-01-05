package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.*
import com.asmirestoran.pesomakanan.model.Employee

class EmployeeRepository {
    val addEmployeeResult: MutableLiveData<Resource<Employee>> =
        MutableLiveData<Resource<Employee>>()

    fun addOrUpdateEmployee(item: Employee) {
        addEmployeeResult.value = Resource.loading(null)
        FirebaseHelper.addOrUpdateEmployee(
            item, {
                addEmployeeResult.value = Resource.success(item)
            }, { _ex ->
                addEmployeeResult.value = Resource.error(_ex.message, null)
            })
    }
}