package com.animesh.justapp

import androidx.compose.material.Typography
import androidx.compose.ui.unit.sp
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.animesh.justapp.data.Expenditure
import com.animesh.justapp.data.FinalFavActivityState
import com.animesh.justapp.data.MenuItem
import com.animesh.justapp.repository.ExpenditureDescription
import com.animesh.justapp.repository.UserFavActivitiesRepository
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.AppBar
import com.animesh.justapp.uicomponents.DrawerBody
import com.animesh.justapp.uicomponents.colors
import com.animesh.justapp.uicomponents.customColors
import com.animesh.justapp.viewmodels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.exp

@AndroidEntryPoint
class HomeScreenActivity : ComponentActivity() {
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button press
            finishAffinity()
        }
    }
    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(scaffoldState = scaffoldState,
                    topBar = {
                        AppBar(
                            onNavigationIconClick = {
                                scope.launch { scaffoldState.drawerState.open() }

                            })
                    },
                    drawerContent = {
                        head()
                        DrawerBody(
                            items = listOf(
                                MenuItem("Home", "Home", Icons.Default.Home),
                                MenuItem("Settings", "Settings", Icons.Default.Settings)
                            ),
                            onItemClick = { println("clicked on ${it.title}") }

                        )
                    }) {

                    StatefulExpenditureView(modifier = Modifier)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Register the back pressed callback with the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onPause() {
        super.onPause()

        // Unregister the back pressed callback with the OnBackPressedDispatcher
        backPressedCallback.isEnabled = false
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun head() {
        val userRepository = UserFavActivitiesRepository()
        var selectImages by remember { mutableStateOf(listOf<Uri>()) }
        val gallerylauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) {
            selectImages = it
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            Column() {
                Row() {
                    var painter = rememberImagePainter(
                        data = "http://192.168.1.2:8080/image/profile",
                        builder = {})
                    if (!selectImages.isEmpty()) {
                        painter = rememberImagePainter(
                            data = selectImages[0],
                            builder = {})
                    }
                    Spacer(Modifier.weight(0.25f))
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(4.dp, 4.dp)
                            .background(colorResource(id = R.color.purple_200), CircleShape)
                            .clip(CircleShape)
                            .clickable { gallerylauncher.launch("image/*") }
                            .size(125.dp),

                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.weight(0.9f))
                    Image(
                        painter = painterResource(id = R.drawable.edit), contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                if (!selectImages.isEmpty()) {
                                    val filedir = applicationContext.filesDir
                                    val file = File(filedir, "image.jpg")
                                    val inputStream =
                                        contentResolver.openInputStream(selectImages[0])
                                    val fileOutputStream = FileOutputStream(file)
                                    inputStream!!.copyTo(fileOutputStream)
                                    userRepository.insertPic(file)
                                }

                            }
                            .padding(5.dp)
                            .size(20.dp),
                        alignment = Alignment.TopEnd,
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StatefulExpenditureView(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    modifier: Modifier
) {

    var showView = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    val expenditures = remember { homeScreenViewModel.expenditures }
    StatelessExpenditureView(
        showView = showView.value,
        text = text,
        number = number,
        expenditures = expenditures,
        onTextValueChanged = { text = it },
        onExpenseValueChanged = { number = it },
        onSaveClick = {
            var expenditure = Expenditure(
                "animesh",
                text,
                number
            )
            showView.value = !showView.value
            homeScreenViewModel.addExpense(expenditure)
            expenditures.add(expenditure)
        },
        onFloatinguttonClick = { showView.value = !showView.value },
        modifier = modifier
    )

}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StatelessExpenditureView(
    showView: Boolean,
    text: String,
    number: String,
    expenditures: List<Expenditure>,
    onTextValueChanged: (String) -> Unit,
    onExpenseValueChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onFloatinguttonClick: () -> Unit,
    modifier: Modifier
) {


    Box() {
        if (!showView) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp)
            ) {
                items(expenditures) { model -> Text(model.expenditureName) }
            }
        }

        if (showView) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Enter Text:",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                OutlinedTextField(
                    modifier = Modifier,
                    value = text,
                    onValueChange = { onTextValueChanged(it) },
                    placeholder = { Text("Enter text") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        backgroundColor = customColors.onPrimary
                    ),
                )

                Text(
                    text = "Enter Number:",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    modifier = Modifier,
                    value = number,
                    onValueChange = { onExpenseValueChanged(it) },
                    placeholder = { },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        backgroundColor = customColors.onPrimary
                    ),
                )
                Button(
                    onClick = { onSaveClick() },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { onFloatinguttonClick() },
            backgroundColor = Color.Red,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    JustAppTheme {

    }
}