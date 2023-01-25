package com.animesh.justapp.network

import com.animesh.justapp.data.FavouriteActivity
import com.animesh.justapp.data.User
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @POST("/register/user")
    suspend fun registerUser(@Body user: User): String

    @POST("/register/login")
    suspend fun login(@Body user: User):String

    @GET("/userinfo/activities")
    suspend fun getFavActivities(@Query ("userid")userid:String):List<FavouriteActivity>

}