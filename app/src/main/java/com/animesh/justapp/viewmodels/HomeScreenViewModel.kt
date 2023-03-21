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
    val userRepo1 = userFavActRepo

    private lateinit var expenditures: MutableList<Expenditure>


    fun getTotalExpenseOnDay(user: String, day: String, month: String, year: String): Int {
        var totalExpenditure = 0
        userRepo1.getExpenditures(user, day, month, year)
            .forEach { totalExpenditure += it.expenditureAmount.toInt() }
        return totalExpenditure
    }

    fun getTotalExpenseForMonth(user: String, month: String, year: String): Int {
        var totalExpenditure = 0
        userRepo1.getExpendituresByMonth(user, month, year)
            .forEach { totalExpenditure += it.expenditureAmount.toInt() }
        return totalExpenditure
    }

    fun getExpenses(
        userId: String,
        day: String,
        month: String,
        year: String
    ): MutableList<Expenditure> {
        expenditures = userRepo1.getExpenditures(userId, day, month, year)
        return expenditures
    }

    fun addExpense(expenditure: Expenditure) {
        userRepo1.addExpenditure(expenditure)
        ///if succesful add to live data

    }

    fun removeExpense(expenditure: Expenditure) {
        userRepo1.removeExpenditure(expenditure)
        expenditures.remove(expenditure)
        ///if succesful add to live data

    }

    fun deleteUser(user: String) {
        userRepo1.deleteUser(user)

        ///if succesful add to live data
    }
    fun deleteUserImages(user: String) {
        userRepo1.deleteUserImages(user)

        ///if succesful add to live data
    }


}