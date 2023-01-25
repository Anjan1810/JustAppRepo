package com.animesh.justapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper
{
    val Base_URL="http://192.168.1.4:8080/"

    fun getInstance(): Retrofit {
        return  Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}