package com.animesh.justapp.network

import android.media.Image
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.data.User
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun login(@Body user: User): JsonObject

    @GET("/userinfo/expenditures")
    suspend fun getExpendituresForUser(
        @Query("userid") userid: String,
        @Query("day") day: String,
        @Query("month") month: String,
        @Query("year") year: String
    ): MutableList<Expenditure>

    @GET("/userinfo/expendituresbymonth")
    suspend fun getExpendituresForUserByMonth(
        @Query("userid") userid: String,
        @Query("month") month: String,
        @Query("year") year: String
    ): MutableList<Expenditure>

    @POST("/userinfo/expenditure/add")
    suspend fun addExpenditure(@Body expenditure: Expenditure): String

    @POST("/userinfo/expenditure/remove")
    suspend fun removeExpenditure(@Body expenditure: Expenditure): String

    @Multipart
    @POST("/image/upload")
    suspend fun insertImage(@Part("userid") userid:String,@Part image: MultipartBody.Part): String

    @GET("/image/{name}")
    suspend fun getProfileImage(@Path("name") userid: String): String

    @GET("/register/delete/{userId}")
    suspend fun deleteUser(@Path("userId") userid: String): String

    @GET("/image/deleteImages/{userId}")
    suspend fun deleteImagesForUser(@Path("userId") userid: String): String

}