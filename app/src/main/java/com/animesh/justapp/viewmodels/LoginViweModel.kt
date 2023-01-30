package com.animesh.justapp.viewmodels

import android.os.Handler
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.UserRepository
import kotlinx.coroutines.*
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoginViweModel : ViewModel() {
    val userRepository = UserRepository()
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    val loadingProgressBar = mutableStateOf(false)

    fun doLogin(user: User): String {
        var str = ""
        loadingProgressBar.value = true
        str = userRepository.doLogin(user)
        loadingProgressBar.value = false

        return str
    }

}