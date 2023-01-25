package com.animesh.justapp.repository

import android.util.Log
import com.animesh.justapp.data.User
import com.animesh.justapp.network.RetrofitHelper
import com.animesh.justapp.network.UserApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserRepository {
    val userApi = RetrofitHelper.getInstance().create(UserApi::class.java)
    fun doRegisterUser(user: User) {


        GlobalScope.launch {
            val result = userApi.registerUser(user)
            if (result != null) {
                Log.d("animesh546546546", result)
            } else {

            }

        }
    }

    fun doLogin(user: User): String {
        var string = ""

        /*runBlocking {
            GlobalScope.launch {
                val result = userApi.login(user)
                if (result != null) {
                    string = result
                }
            }
        }*/
        runBlocking {
            string = async { userApi.login(user) }.await()
        }


        return string


    }


}