package com.animesh.justapp.repository

import android.util.Log
import com.animesh.justapp.data.FavouriteActivity
import com.animesh.justapp.data.FinalFavActivityState
import com.animesh.justapp.network.RetrofitHelper
import com.animesh.justapp.network.UserApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserFavActivitiesRepository @Inject constructor(){
    val userApi = RetrofitHelper.getInstance().create(UserApi::class.java)


    fun getFavouriteActivities(user: String):List<FavouriteActivity>{
        var result= listOf <FavouriteActivity>()
//        GlobalScope.launch {
//            result = userApi.getFavActivities(user)
//            if (result.get(0) != null) {
//                Log.d("animesh546546546", result.get(0).activityName)
//            } else {
//
//            }
//        }
        runBlocking {
            result = async { userApi.getFavActivities(user) }.await()
        }
        return  result
    }


    fun getFavouriteActivitiesDescription(): Flow<FavouriteActivityDescription> {

        return flow {
            // val favactivity = FavouriteActivity("aaa", "playing", "4")
            //  val favactivity1 = FavouriteActivity("aaa", "shooting", "4")
            val favactivitydesc = FavouriteActivityDescription("hjgjhfgjhgj")
            val favactivitydesc1 = FavouriteActivityDescription("kjhkjhkjhkjh")
            var list = mutableListOf<FavouriteActivityDescription>(favactivitydesc)
            list.add(favactivitydesc1)
            //val result = userApi.getFavActivities(user)

            emit(favactivitydesc)

            emit(favactivitydesc1)

        }
    }

//    fun getCombinedFlow(): Flow<List<FinalFavActivityState>> {
//        return flow {
//            combine(
//                getFavouriteActivities(""),
//                getFavouriteActivitiesDescription(),
//                { aa, cc -> FinalFavActivityState(aa.get(0), cc) })
//        }
//        //val flow1 = getFavouriteActivities("")
//
//    }
}

data class FavouriteActivityDescription(

    val description: String
)