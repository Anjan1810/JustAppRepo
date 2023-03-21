package com.animesh.justapp.viewmodels

import android.os.Handler
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.UserRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoginViweModel : ViewModel() {
    val userRepository = UserRepository()

    private val _username = mutableStateOf("")
    val username: MutableState<String>
        get() = _username
    private val _email = mutableStateOf("")
    val email: MutableState<String>
        get() = _email
    private val _password = mutableStateOf("")
    val password: MutableState<String>
        get() = _password

    val loadingProgressBar = mutableStateOf(false)

    fun doLogin(user: User): JsonObject {
        var str = JsonObject()
        loadingProgressBar.value = true
        str = userRepository.doLogin(user)
        loadingProgressBar.value = false

        return str
    }

}