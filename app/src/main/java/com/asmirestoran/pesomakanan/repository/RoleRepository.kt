package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.User

object RoleRepository {
    val rolesData: MutableLiveData<List<User.Role>> = MutableLiveData()

    init {
        if (rolesData.value == null) {
            val roles: List<User.Role> =
                mutableListOf(User.Role.SELECT, User.Role.ADMIN, User.Role.CASHIER, User.Role.CLERK)
            rolesData.value = roles
        }
    }
}