package com.animesh.justapp.viewmodels

import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.data.FinalFavActivityState
import com.animesh.justapp.repository.ExpenditureDescription
import com.animesh.justapp.repository.UserFavActivitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(userFavActRepo: UserFavActivitiesRepository) :
    ViewModel() {
    val userRepo = UserFavActivitiesRepository()
    val userRepo1 = userFavActRepo
    lateinit var expenditureText: String
    lateinit var expenseAmount: String

    var userid: String = "animesh"
    val expenditures = userRepo1.getExpenditures(userid)

    val liveDataList = MutableLiveData<List<Expenditure>>(listOf(
        Expenditure("Item 1","","")
    ))

    fun addExpense(expenditure: Expenditure) {
        userRepo1.addExpenditure(expenditure)
        ///if succesful add to live data

    }
    fun addExpensde(expenditure: Expenditure) {
        userRepo1.addExpenditure(expenditure)
        ///if succesful add to live data

    }



}