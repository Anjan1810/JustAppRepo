package com.animesh.justapp.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.network.RetrofitHelper
import com.animesh.justapp.network.UserApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserFavActivitiesRepository @Inject constructor() {
    val userApi = RetrofitHelper.getInstance().create(UserApi::class.java)


    fun getExpenditures(user: String,day:String,month:String,year:String): MutableList<Expenditure> {
        var result = mutableListOf<Expenditure>()
        runBlocking {
            result = async { userApi.getExpendituresForUser(user,day,month,year) }.await()
        }
        return result
    }

    fun addExpenditure(expenditure: Expenditure):String{
        var result = ""
        runBlocking {
            result = async { userApi.addExpenditure(expenditure) }.await()
        }
        return result
    }

    fun removeExpenditure(expenditure: Expenditure):String{
        var result = ""
        runBlocking {
            result = async { userApi.removeExpenditure(expenditure) }.await()
        }
        return result
    }


    fun getFavouriteActivitiesDescription(): Flow<ExpenditureDescription> {

        return flow {
            // val favactivity = Expenditure("aaa", "playing", "4")
            //  val favactivity1 = Expenditure("aaa", "shooting", "4")
            val favactivitydesc = ExpenditureDescription("hjgjhfgjhgj")
            val favactivitydesc1 = ExpenditureDescription("kjhkjhkjhkjh")
            var list = mutableListOf<ExpenditureDescription>(favactivitydesc)
            list.add(favactivitydesc1)
            //val result = userApi.getFavActivities(user)

            emit(favactivitydesc)

            emit(favactivitydesc1)

        }
    }

    fun insertPic(file: File): String {

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        var result = ""
        runBlocking {
            result = async { userApi.insertImage(body) }.await()
        }
        return result
    }

    fun downloadPic(filename: String): String {

        var result = ""
        runBlocking {
            result = async { userApi.getProfileImage(filename) }.await()
        }
        return result
    }

}

data class ExpenditureDescription(

    val description: String
)