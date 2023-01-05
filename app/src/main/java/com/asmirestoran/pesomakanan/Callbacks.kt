package com.asmirestoran.pesomakanan

import com.asmirestoran.pesomakanan.model.Employee

interface UserDetailsCallback {
    fun onUserDetailsLoaded(user: User)
    fun onUserDetailsFailed()
}
interface EmployeeDetailsCallback {
    fun onDetailsLoaded(employee: Employee)
    fun onLoadFailed(error: String)
}
