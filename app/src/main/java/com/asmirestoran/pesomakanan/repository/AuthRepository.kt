package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.*
import com.asmirestoran.pesomakanan.local.PreferenceHelper
import com.google.firebase.auth.FirebaseAuth

object AuthRepository {
    val signupResult: MutableLiveData<Event<Resource<User>>> =
        MutableLiveData<Event<Resource<User>>>()
    val loginResult: MutableLiveData<Event<Resource<User>>> =
        MutableLiveData<Event<Resource<User>>>()
     var currentUser: User? = null

    fun signup(appUser: User) {
        signupResult.value = Event(Resource.loading(null))
        FirebaseHelper.createUser(appUser) {
            if (it.isSuccessful) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                firebaseUser?.let { _user ->
                    appUser.firebaseId = _user.uid
                    FirebaseHelper.addUser(appUser, {
                        signupResult.value = Event(Resource.success(appUser))
                    }, { _ex ->
                        signupResult.value = Event(Resource.error(_ex.message, null))
                    })
                }
            } else {
                signupResult.value = Event(Resource.error(it.exception?.message, null))
            }
        }
    }

    fun login(appUser: User) {
        loginResult.value = Event(Resource.loading(null))
        FirebaseHelper.login(appUser) {
            if (it.isSuccessful) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                firebaseUser?.let { _user ->
                    appUser.firebaseId = _user.uid
                    FirebaseHelper.getUserDetail(appUser, object : UserDetailsCallback {
                        override fun onUserDetailsLoaded(user: User) {
                            currentUser = user
                            PreferenceHelper.setCurrentUser(currentUser)
                            loginResult.value = Event(Resource.success(currentUser))
                        }

                        override fun onUserDetailsFailed() {
                            loginResult.value = Event(Resource.error(it.exception?.message, null))
                        }
                    })
                }
            } else {
                loginResult.value = Event(Resource.error(it.exception?.message, null))
            }
        }
    }
}