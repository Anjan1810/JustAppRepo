package com.animesh.justapp.network

import android.media.Image
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.data.User
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @POST("/register/user")
    suspend fun registerUser(@Body user: User): String

    @POST("/register/login")
    suspend fun login(@Body user: User):String

    @GET("/userinfo/expenditures")
    suspend fun getExpendituresForUser(@Query ("userid")userid:String):MutableList<Expenditure>

    @POST("/userinfo/expenditure/add")
    suspend fun addExpenditure(@Body expenditure: Expenditure):String

    @Multipart
    @POST("/image")
    suspend fun  insertImage(@Part image: MultipartBody.Part):String

    @GET("/image/{name}")
    suspend fun getProfileImage(@Path ("name")userid:String): String

}