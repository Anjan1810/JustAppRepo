package com.animesh.justapp.viewmodels

import androidx.lifecycle.ViewModel
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.UserRepository

class RegisterViewModel : ViewModel() {
    val userRepository = UserRepository()
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    fun register(user: User) {
        userRepository.doRegisterUser(user)
    }

}