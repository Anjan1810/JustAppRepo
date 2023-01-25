package com.animesh.justapp.viewmodels

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.justapp.data.FavouriteActivity
import com.animesh.justapp.data.FinalFavActivityState
import com.animesh.justapp.repository.FavouriteActivityDescription
import com.animesh.justapp.repository.UserFavActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(userFavActRepo:UserFavActivitiesRepository) : ViewModel() {


    var userid: String = "animesh"
    val favouriteActivity = userFavActRepo.getFavouriteActivities(userid)
    val favouriteActivityDescription =
        userFavActRepo.getFavouriteActivitiesDescription()


}