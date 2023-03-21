package com.animesh.justapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.material.Typography
import androidx.compose.ui.unit.sp
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.LimitExceededException
import android.util.Log
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.animesh.justapp.viewmodels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.justapp.network.RetrofitHelper
import com.animesh.justapp.repository.DataStoreManager
import com.animesh.justapp.uicomponents.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                val context = LocalContext.current
                val saveUser = remember {
                    mutableStateOf(false)
                }
                var store = DataStoreManager(LocalContext.current)
                if (saveUser.value) {
                    LaunchedEffect(Unit) {
                        launch {
                            store.saveUser("")

                        }
                    }
                }
                val scaffoldState = rememberScaffoldState()
                val user = store.getUser.collectAsState(initial = "")

                var showAlert = remember { mutableStateOf(false) }
                DeleteConfirmationDialog(
                    modifier = Modifier,
                    showView = showAlert.value,
                    stringResource(id = R.string.delete_item),
                    onConfirm = {
                        homeScreenViewModel.deleteUser(user.value.removeSurrounding("\""))
                        homeScreenViewModel.deleteUserImages(user.value.removeSurrounding("\""))
                        context.startActivity(
                            Intent(
                                context, MainActivity::class.java
                            )
                        )
                        saveUser.value = true
                        showAlert.value = false
                    },
                    onCancel = { showAlert.value = false })

                val scope = rememberCoroutineScope()
                Scaffold(scaffoldState = scaffoldState, topBar = {
                    AppBar(onNavigationIconClick = {
                        scope.launch { scaffoldState.drawerState.open() }

                    })
                }, drawerContent = {
                    head(scaffoldState, scope)
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 10.dp)
                    )
                    DrawerBody(
                        items = listOf(
                            MenuItem("Home", "Home", Icons.Default.Home),
                            MenuItem("Settings", "Settings", Icons.Default.Settings),
                            MenuItem("Log Out", "Log Out", Icons.Filled.ArrowBack),
                            MenuItem(
                                "Delete Account", "Delete Account", Icons.Filled.Delete
                            )


                        ), onItemClick = {
                            if (it.title.equals("Settings")) {
                                context.startActivity(
                                    Intent(
                                        context, SettingsScreen::class.java
                                    )
                                )
                            }
                            if (it.title.equals("Home")) {
                                context.startActivity(
                                    Intent(
                                        context, HomeScreenActivity::class.java
                                    )
                                )
                            }
                            if (it.title.equals("Log Out")) {
                                saveUser.value = true
                                context.startActivity(
                                    Intent(
                                        context, MainActivity::class.java
                                    )
                                )
                            }

                            if (it.title.equals("Delete Account")) {
                                showAlert.value = true

                            }
                        })
                }) {

                    Column() {
                        var mDay =
                            rememberSaveable { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }
                        val mMonth =
                            rememberSaveable { mutableStateOf(LocalDate.now().monthValue.toString()) }
                        val mYear =
                            rememberSaveable { mutableStateOf(LocalDate.now().year.toString()) }
                        calendar(onDateChanged = { day, month, year ->
                            mDay.value = day.toString()
                            mMonth.value = month.toString()
                            mYear.value = year.toString()
                        })

                        Spacer(modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp))

                        StatefulExpenditureView(
                            homeScreenViewModel,
                            mDay.value,
                            mMonth.value,
                            mYear.value,
                            modifier = Modifier
                        )
                    }

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


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun calendar(
        onDateChanged: (Int, Int, Int) -> Unit
    ) {
        val mContext = LocalContext.current

        val mYear: Int
        val mMonth: Int
        val mDay: Int

        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()

        // Fetching current year, month and day
        mYear = mCalendar.get(Calendar.YEAR)
        mMonth = mCalendar.get(Calendar.MONTH)
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        mCalendar.time = Date()

        val mDate = rememberSaveable {
            mutableStateOf(
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString()
            )
        }

        val mDatePickerDialog = DatePickerDialog(
            mContext, { _, year, month, dayOfMonth ->
                // Your code here, which will be executed when the date is set.
                // You can use the new values of year, month, and dayOfMonth to perform some action.
                mDate.value = "$dayOfMonth/${month + 1}/$year"
                onDateChanged(dayOfMonth, (month + 1), year)
            }, mYear, mMonth, mDay
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Displaying the mDate value in the Text
            Text(
                text = "${mDate.value}",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.teko_semibold))
            )
            // Adding a space of 100dp height
            Spacer(modifier = Modifier.size(10.dp))

            Icon(
                Icons.Filled.Edit,
                contentDescription = "Calendar icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { mDatePickerDialog.show() },
                tint = Color.Red
            )
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun head(scaffoldState: ScaffoldState, scope: CoroutineScope) {
        val context = LocalContext.current
        val store = DataStoreManager(context)
        var user = store.getUser.collectAsState(initial = "")

        val userRepository = UserFavActivitiesRepository()
        var selectImages by remember { mutableStateOf(listOf<Uri>()) }
        var showButton = remember {
            mutableStateOf(false)
        }
        val gallerylauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) {
            selectImages = it
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(customColors.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp)) {
                Row() {
//                    var painter =
//                        rememberImagePainter(data = RetrofitHelper.Base_URL + "image/" + user.value.removeSurrounding(
//                            "\""
//                        ),
//                            builder = { placeholder(R.drawable.app_icon) })
//                    if (!selectImages.isEmpty()) {
//                        painter = rememberImagePainter(data = selectImages[0],
//                            builder = { placeholder(R.drawable.app_icon) })
//                    }
                    Column() {

                        Box() {
//                            Image(painter = painterResource(id = R.drawable.app_icon),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .padding(4.dp, 4.dp)
//                                    // .background(colorResource(id = R.color.purple_200), CircleShape)
//                                    .clip(CircleShape)
//                                    .clickable { gallerylauncher.launch("image/*") }
//                                    .size(125.dp),
//
//                                contentScale = ContentScale.Crop)
//                            Image(painter = painter,
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .padding(4.dp, 4.dp)
//                                    // .background(colorResource(id = R.color.purple_200), CircleShape)
//                                    .clip(CircleShape)
//                                    .clickable { gallerylauncher.launch("image/*") }
//                                    .size(125.dp),
//
//                                contentScale = ContentScale.Crop)

                            Image(painter = painterResource(id = R.drawable.app_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp, 4.dp)
                                    // .background(colorResource(id = R.color.purple_200), CircleShape)
                                    .clip(CircleShape)
                                    .clickable { gallerylauncher.launch("image/*") }
                                    .size(125.dp), contentScale = ContentScale.Crop)

                            CoilImage(imageUrl = RetrofitHelper.Base_URL + "image/" + user.value.removeSurrounding(
                                "\""
                            ),
                                contentDescription = null,
                                onImageClick = { gallerylauncher.launch("image/*") })

                            if (!selectImages.isEmpty()) {
                                showButton.value=true
                                CoilImage(
                                    imageUrl = selectImages[0].toString(),
                                    contentDescription = null,
                                    onImageClick = { gallerylauncher.launch("image/*") })
                            }

                        }

                    }

                    //Spacer(Modifier.weight(0.65f))
                    //if (!selectImages.isEmpty()) {
                    if (showButton.value) {
                        Button(onClick = {
                            if (!selectImages.isEmpty()) {
                                val filedir = applicationContext.filesDir
                                val file = File(filedir, "image.jpg")
                                val inputStream = contentResolver.openInputStream(selectImages[0])
                                val fileOutputStream = FileOutputStream(file)
                                inputStream!!.copyTo(fileOutputStream)
                                userRepository.insertPic(file, user.value.removeSurrounding("\""))
                                selectImages.dropWhile { selectImages.isNotEmpty() }
                                scope.launch { scaffoldState.drawerState.close() }
                                showButton.value = false
                            }
                        }) {
                            Text(text = "Save")

                        }
                    }


                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatefulExpenditureView(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    mDate: String,
    mMonth: String,
    mYear: String,
    modifier: Modifier
) {

    val context = LocalContext.current
    val store = DataStoreManager(context)
    var theme = store.getTheme.collectAsState(initial = "")
    var currency = store.getCurrency.collectAsState(initial = "INR")
    var user = store.getUser.collectAsState(initial = "")

    var showView = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var showAlert = remember { mutableStateOf(false) }
    val expenditures = homeScreenViewModel.getExpenses(
        user.value.toString(), mDate, mMonth, mYear
    )
    var total = homeScreenViewModel.getTotalExpenseOnDay(
        user.value.toString(), mDate, mMonth, mYear
    )
    var totalByMonth = homeScreenViewModel.getTotalExpenseForMonth(
        user.value.toString(), mMonth, mYear
    )

    Text(
        "Total Expenditure On this day-${total}(${
            currency.value
        })", modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
            .fillMaxWidth()
            .background(
                customColors.onPrimary
            ), style = TextStyle(
            fontFamily = FontFamily(Font(R.font.teko_semibold)),
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            color = fontcolors.primary
        ), textAlign = TextAlign.Center

    )
    Spacer(modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp))
    Text(
        "Total Expenditure this month-${totalByMonth}(${
            currency.value
        })", modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
            .fillMaxWidth()
            .background(
                customColors.onPrimary
            ), style = TextStyle(
            fontFamily = FontFamily(Font(R.font.teko_semibold)),
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            color = fontcolors.primary
        ), textAlign = TextAlign.Center

    )

    Spacer(modifier = Modifier.padding(5.dp, 5.dp, 5.dp, 5.dp))

    var expense by remember { mutableStateOf<Expenditure>(Expenditure("", "", "", "", "", "")) }
    StatelessExpenditureView(
        showView = showView.value,
        text = text,
        number = number,
        expenditures = expenditures,
        onTextValueChanged = { text = it },
        onExpenseValueChanged = { newValue ->
            if (newValue.length <= 6) {
                number = newValue
            }
        },
        onSaveClick = {
            if (text.isNullOrEmpty() || number.isNullOrEmpty()) {
                Toast.makeText(context, "Please Enter required details", Toast.LENGTH_SHORT).show()
            } else {
                var expenditure = Expenditure(
                    user.value, text, number, mDate, mMonth, mYear
                )
                showView.value = !showView.value
                homeScreenViewModel.addExpense(expenditure)
                expenditures.add(expenditure)
            }


        },
        onFloatinguttonClick = { showView.value = !showView.value },
        onDeleteClick = { it ->
            expense = it
            showAlert.value = true
        },
        theme.value,
        modifier = modifier
    )

    DeleteConfirmationDialog(
        modifier = modifier,
        showView = showAlert.value,
        stringResource(id = R.string.delete_item),
        onConfirm = {
            homeScreenViewModel.removeExpense(expense)
            showAlert.value = false
        },
        onCancel = { showAlert.value = false })


}

@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier,
    showView: Boolean,
    string: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    if (showView) {
        AlertDialog(onDismissRequest = onCancel, title = {
            Text(
                "Confirm Deletion", style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
        }, text = {
            Text(
                text = string, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            )
        }, confirmButton = {
            Button(onClick = {
                onConfirm()

            }) {
                Text("OK")
            }
        }, dismissButton = {
            Button(onClick = {
                onCancel()
            }) {
                Text("Cancel")
            }
        })
    }


}


@RequiresApi(Build.VERSION_CODES.O)
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
    onDeleteClick: (Expenditure) -> Unit,
    color: String,
    modifier: Modifier
) {
    var colorTheme = Color.White
    if (color.equals("1")) {
        colorTheme = Color.Black
    }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.background(colorTheme, RectangleShape)) {
        Spacer(modifier = modifier.padding(25.dp))
        if (!showView) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 5.dp, 20.dp, 25.dp)
                    .clickable { },
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(expenditures) { index, item ->
                    ExpenditureItem(expenditure = item,
                        FontFamily(Font(R.font.handlee_regular)),
                        onDelete = { onDeleteClick(item) })
                }
            }
        }

        if (showView) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Expense:",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.Black
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .heightIn(100.dp, 350.dp),
                    value = text,
                    onValueChange = { onTextValueChanged(it) },
                    placeholder = { Text("Expense") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        backgroundColor = customColors.onPrimary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    })
                )

                Text(
                    text = "Amount:",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        color = Color.Black
                    ),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .heightIn(100.dp, 350.dp),
                    value = number,
                    singleLine = true,
                    onValueChange = { onExpenseValueChanged(it) },
                    placeholder = { Text("Amount") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        backgroundColor = customColors.onPrimary
                    ),

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    })

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
            backgroundColor = Color.Green,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    JustAppTheme {

    }
}