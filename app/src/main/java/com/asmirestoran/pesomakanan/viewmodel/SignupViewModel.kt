package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.User
import com.asmirestoran.pesomakanan.repository.AuthRepository
import com.asmirestoran.pesomakanan.repository.RoleRepository

class SignupViewModel : ViewModel() {
    val user = MutableLiveData<User>()
    val result = AuthRepository.signupResult
    val rolesData: MutableLiveData<List<User.Role>>
        get() = RoleRepository.rolesData

    init {
        user.value = User().apply {
            emailId = "suren@gmail.com"
            password = "Test@123"
            name = "Test"
            role = User.Role.ADMIN
            confirmPassword = "Test@123"
        }
    }

    fun signup(user: User) {
        AuthRepository.signup(user)
    }
}