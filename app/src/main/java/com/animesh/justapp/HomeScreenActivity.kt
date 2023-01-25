package com.animesh.justapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.animesh.justapp.data.FavouriteActivity
import com.animesh.justapp.data.FinalFavActivityState
import com.animesh.justapp.repository.FavouriteActivityDescription
import com.animesh.justapp.repository.UserFavActivitiesRepository
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.viewmodels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeScreenActivity : ComponentActivity() {
    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SetUpFavouriteActivities(homeScreenViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SetUpFavouriteActivities(homeScreenViewModel: HomeScreenViewModel) {


    val favActivity= homeScreenViewModel.favouriteActivity

    val favActivitydesc: FavouriteActivityDescription by homeScreenViewModel.favouriteActivityDescription.collectAsState(
        initial = FavouriteActivityDescription("")
    )

    val list = remember {
        mutableStateListOf(FinalFavActivityState(
            FavouriteActivity("","",""),
            FavouriteActivityDescription("")
        ))
    }

   // val finalFavActivityState by homeScreenViewModel.combinedflow.collectAsStateWithLifecycle()

    //list.add(favActivity)


    Box() {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(50.dp)) {
            items(favActivity) { model -> Text(model.rating) }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    JustAppTheme {

    }
}