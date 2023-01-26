package com.animesh.justapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViweModel : ViewModel() {
    val userRepository = UserRepository()
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    val loadingProgressBar = mutableStateOf(false)

    fun doLogin(user: User): String {
        var str = ""
        viewModelScope.launch {
            loadingProgressBar.value = true
            str = userRepository.doLogin(user)
            loadingProgressBar.value = false
        }
        return str
    }

}