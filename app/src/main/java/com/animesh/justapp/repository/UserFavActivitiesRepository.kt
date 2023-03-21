package com.animesh.justapp.repository

import android.util.Log
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.network.RetrofitHelper
import com.animesh.justapp.network.UserApi
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


    fun getExpenditures(
        user: String,
        day: String,
        month: String,
        year: String
    ): MutableList<Expenditure> {
        var result = mutableListOf<Expenditure>()
        runBlocking {
            result = async { userApi.getExpendituresForUser(user, day, month, year) }.await()
        }
        return result
    }

    fun getExpendituresByMonth(
        user: String,
        month: String,
        year: String
    ): MutableList<Expenditure> {
        var result = mutableListOf<Expenditure>()
        runBlocking {
            result = async { userApi.getExpendituresForUserByMonth(user, month, year) }.await()
        }
        return result
    }

    fun addExpenditure(expenditure: Expenditure): String {
        var result = ""
        runBlocking {
            result = async { userApi.addExpenditure(expenditure) }.await()
        }
        return result
    }

    fun removeExpenditure(expenditure: Expenditure): String {
        var result = ""
        runBlocking {
            result = async { userApi.removeExpenditure(expenditure) }.await()
        }
        return result
    }

    fun deleteUser(user: String): String {
        var result = ""
        runBlocking {
            result = async { userApi.deleteUser(user) }.await()
        }
        return result
    }

    fun deleteUserImages(user: String): String {
        var result = ""
        runBlocking {
            result = async { userApi.deleteImagesForUser(user) }.await()
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

    fun insertPic(file: File, user: String): String {
        Log.d("aniiiiiii", user)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        //  val userIdRequest = RequestBody.create(MediaType.parse("text/plain"), user)

        var result = ""
        runBlocking {
            result = async { userApi.insertImage(user.removeSurrounding("\""), body) }.await()
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