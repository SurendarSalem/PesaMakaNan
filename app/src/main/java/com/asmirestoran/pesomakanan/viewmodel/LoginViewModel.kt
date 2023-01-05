package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.User
import com.asmirestoran.pesomakanan.UserDetailsCallback
import com.asmirestoran.pesomakanan.repository.AuthRepository

class LoginViewModel : ViewModel() {
    val user = MutableLiveData<User>()
    val error = MutableLiveData<String>()
    val loginResult = AuthRepository.loginResult
    init {
        user.value = User()
    }

    fun login(user: User) {
        AuthRepository.login(user)
    }
}