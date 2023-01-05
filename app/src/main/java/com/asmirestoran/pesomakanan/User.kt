package com.asmirestoran.pesomakanan

import android.util.Patterns

class User {

    var name: String = ""
        get() = field
        set(value) {
            field = value
        }
    var emailId: String = ""
        set(value) {
            field = value
        }
    var password: String = ""
        set(value) {
            field = value
        }

    var confirmPassword: String = ""
        set(value) {
            field = value
        }

    var firebaseId: String = ""
        set(value) {
            field = value
        }

    var role: Role = Role.SELECT
        set(value) {
            field = value
        }

    enum class Role(val role: String) {
        SELECT("Select a role"),
        ADMIN("Admin"),
        CASHIER("Cashier"),
        CLERK("Clerk");
    }

    companion object {

        fun validForLogin(user: User): String {
            if (user.emailId.isEmpty()) {
                return "Please enter email id"
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(user.emailId).matches()) {
                return "Please enter valid email id"
            }
            if (user.password.isEmpty()) {
                return "Please enter password"
            }
            return ""
        }

        fun validForSignup(user: User): String {
            if (user.name.isEmpty()) {
                return "Please enter name"
            }
            if (user.emailId.isEmpty()) {
                return "Please enter email id"
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(user.emailId).matches()) {
                return "Please enter valid email id"
            }
            if (user.password.isEmpty()) {
                return "Please enter password"
            }
            if (user.confirmPassword.isEmpty()) {
                return "Please enter confirm password"
            }
            if (user.password != user.confirmPassword) {
                return "Password mismatch"
            }
            if (user.role == Role.SELECT) {
                return "Please select a role"
            }
            return ""
        }
    }
}


