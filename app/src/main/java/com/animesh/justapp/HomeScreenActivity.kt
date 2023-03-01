package com.animesh.justapp

import android.app.DatePickerDialog
import androidx.compose.material.Typography
import androidx.compose.ui.unit.sp
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
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
import com.animesh.justapp.viewmodels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.justapp.uicomponents.*
import java.time.LocalDate
import java.time.LocalDateTime
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

                    Column() {
                        // val mDate = remember { mutableStateOf(LocalDate.now().toString()) }
                        var mDay =
                            remember { mutableStateOf(LocalDate.now().dayOfMonth.toString()) }
                        val mMonth =
                            remember { mutableStateOf(LocalDate.now().monthValue.toString()) }
                        val mYear = remember { mutableStateOf(LocalDate.now().year.toString()) }
                        calendar(onDateChanged = { year, month, day ->
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


    @Composable
    fun totalExpenseView(){

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



        val mDate = remember { mutableStateOf(LocalDate.now().toString()) }


        val mDatePickerDialog = DatePickerDialog(
            mContext,
            { _, year, month, dayOfMonth ->
                // Your code here, which will be executed when the date is set.
                // You can use the new values of year, month, and dayOfMonth to perform some action.
                mDate.value = "$dayOfMonth/${month + 1}/$year"
                onDateChanged(year, (month + 1), dayOfMonth)
            }, mYear, mMonth, mDay
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                Icons.Filled.Edit,
                contentDescription = "Calendar icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { mDatePickerDialog.show() },
                tint = Color.Red
            )

            // Adding a space of 100dp height
            Spacer(modifier = Modifier.size(10.dp))

            // Displaying the mDate value in the Text
            Text(
                text = "Date: ${mDate.value}",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.teko_semibold))
            )
        }
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
                        data = "http://192.168.1.6:8080/image/profile",
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
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = null,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatefulExpenditureView(
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
    mDate: String,
    mMonth: String,
    mYear: String,
    modifier: Modifier
) {

    var showView = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var showAlert = remember { mutableStateOf(false) }
    val expenditures = homeScreenViewModel.getExpenses(
        "animesh",
        mDate,
        mMonth,
        mYear
    )
    var total = homeScreenViewModel.getTotalExpenseOnDay(
                "animesh",
                mDate,
                mMonth,
                mYear
            )


    Text(
        "Total Expenditure On this day-${
            total
        }",
        modifier = Modifier
            .padding(10.dp, 5.dp, 10.dp, 5.dp)
            .fillMaxWidth()
            .background(
                customColors.onPrimary
            ),
        style = TextStyle(
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
        onExpenseValueChanged = { number = it },
        onSaveClick = {
            var expenditure = Expenditure(
                "animesh",
                text,
                number,
                mDate,
                mMonth,
                mYear
            )
            showView.value = !showView.value
            homeScreenViewModel.addExpense(expenditure)
            expenditures.add(expenditure)
        },
        onFloatinguttonClick = { showView.value = !showView.value },
        onDeleteClick = { it ->
            expense = it
            showAlert.value = true
        },
        modifier = modifier
    )

    DeleteConfirmationDialog(
        modifier = modifier,
        showView = showAlert.value,
        onConfirm = {
            homeScreenViewModel.removeExpense(expense)
            showAlert.value = false
        }, onCancel = { showAlert.value = false })


}

@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier,
    showView: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    if (showView) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = {
                Text(
                    "Confirm Deletion", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            },
            text = {
                Text(
                    "${R.string.delete_item}", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.handlee_regular)),
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()

                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancel()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
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
    modifier: Modifier
) {


    Box() {
        Spacer(modifier = modifier.padding(25.dp))
        if (!showView) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp, 5.dp, 20.dp, 25.dp)
                    .clickable { }, verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(expenditures) { index, item ->
                    ExpenditureItem(
                        expenditure = item,
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
                    .verticalScroll(rememberScrollState())
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
                    )
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
                    onValueChange = { onExpenseValueChanged(it) },
                    placeholder = { Text("Amount") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        cursorColor = MaterialTheme.colors.primary,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        backgroundColor = customColors.onPrimary
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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