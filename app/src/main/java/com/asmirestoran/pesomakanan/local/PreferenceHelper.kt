package com.asmirestoran.pesomakanan.local

import android.content.Context
import android.content.SharedPreferences
import com.asmirestoran.pesomakanan.User
import com.asmirestoran.pesomakanan.repository.AuthRepository.currentUser
import com.google.gson.Gson

object PreferenceHelper {
    private const val USER: String = "user"
    private const val IS_LOGIN = "IS_LOGIN"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("Resrotan", Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGIN, false)
        set(value) = sharedPreferences.edit {
            it.putBoolean(IS_LOGIN, value)
        }

    fun setCurrentUser(user: User?) {
        isLogin = true
        sharedPreferences.edit().putString(USER, Gson().toJson(user)).apply()
    }

    fun getCurrentUser(): User? {
        if (currentUser == null) {
            val gson = Gson()
            currentUser = gson.fromJson(
                sharedPreferences.getString(USER, ""),
                User::class.java
            )
        }
        return currentUser
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}