package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Employee
import com.asmirestoran.pesomakanan.repository.EmployeeRepository

class AddEmployeeViewModel : ViewModel() {
    val employeeLiveData = MutableLiveData<Employee>()

    val error = MutableLiveData<String>()
    private val employeeRepository = EmployeeRepository()
    val addEmployeeResult = employeeRepository.addEmployeeResult

    init {
        employeeLiveData.value = Employee().apply {
            employeeId = Employee.createId()
        }
    }

    fun addEmployee() {
        employeeLiveData.value?.let {
            employeeRepository.addOrUpdateEmployee(it)
        }
    }

    fun reset() {
        employeeLiveData.value = Employee()
    }
}