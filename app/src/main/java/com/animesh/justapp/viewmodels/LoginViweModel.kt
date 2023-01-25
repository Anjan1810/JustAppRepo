package com.animesh.justapp.viewmodels

import androidx.lifecycle.ViewModel
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.UserRepository

class LoginViweModel : ViewModel() {
    val userRepository = UserRepository()
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    fun doLogin(user: User): String {
        val str = userRepository.doLogin(user)
        return str
    }

}