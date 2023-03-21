package com.animesh.justapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.justapp.data.MenuItem
import com.animesh.justapp.data.User
import com.animesh.justapp.repository.DataStoreManager
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.*
import com.animesh.justapp.viewmodels.LoginViweModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button press
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val context = LocalContext.current
                    val store = DataStoreManager(context)
                    var user = store.getUser.collectAsState(initial = "")
                    if (user.value == "") {
                        StatefulLoginRegisterScreen()
                    } else {
                        context.startActivity(Intent(context, HomeScreenActivity::class.java))
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
}

@Composable
fun StatefulLoginRegisterScreen(loginViewModel: LoginViweModel = viewModel()) {
    val context = LocalContext.current
    val store = DataStoreManager(context)
    val saveUser = remember {
        mutableStateOf(false)
    }
    var userId = remember {
        mutableStateOf("")
    }
    var isDisplayed = remember { mutableStateOf(false) }



    if (saveUser.value) {
        LaunchedEffect(Unit) {
            launch {
                store.saveUser(userId.value.toString())

            }
        }
    }
    StatelessLoginRegisterScreen(
        id = loginViewModel.email.value,
        password = loginViewModel.password.value,
        onloginClick = {
            if (loginViewModel.email.value.isEmpty() || loginViewModel.password.value.isEmpty()) {
                Toast.makeText(context, "Please enter credentials", Toast.LENGTH_SHORT).show()

            } else {
                isDisplayed.value = true


//                val result = loginViewModel.doLogin(
//                    User(
//                        "", loginViewModel.email.value, loginViewModel.password.value
//                    )
//                )
//
//                if (result.get("emailId") != null) {
//
//                    userId.value = result.get("emailId").toString()
//                    saveUser.value = true
//                    isDisplayed.value = false
//                    context.startActivity(
//                        Intent(
//                            context, HomeScreenActivity::class.java
//                        )
//                    )
//                }
            }

        },
        onRegisterClick = { context.startActivity(Intent(context, RegisterActivity::class.java)) },
        onLoginValueChange = { newid -> loginViewModel.email.value = newid },
        onPswdValChange = { newpswd -> loginViewModel.password.value = newpswd },
        modifier = Modifier
    )
    if (isDisplayed.value) {
        CircularProgressBar()
        val result = loginViewModel.doLogin(
            User(
                "", loginViewModel.email.value, loginViewModel.password.value
            )
        )

        if (result.get("emailId") != null) {
            userId.value = result.get("emailId").toString()
            saveUser.value = true
            isDisplayed.value = false
            context.startActivity(
                Intent(
                    context, HomeScreenActivity::class.java
                )
            )
        }
    }

}

@Composable
fun StatelessLoginRegisterScreen(
    id: String,
    password: String,
    onloginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginValueChange: (String) -> Unit,
    onPswdValChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(customColors.onPrimary)) {

        Column(
            modifier = modifier
                .padding(25.dp, 50.dp, 25.dp, 0.dp)
                .fillMaxHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "",
                modifier = Modifier
                    .padding(4.dp, 4.dp)
                    .background(colorResource(id = R.color.purple_200), CircleShape)
                    .clip(CircleShape)
                    .size(125.dp),

                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Email Id",
                    modifier.weight(0.29F, true),
                    fontFamily = FontFamily(Font(R.font.teko_semibold)),
                    fontSize = 24.sp
                )

                OutlinedTextField(modifier = modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(customColors.secondary)
                    ),
                    value = id,
                    onValueChange = { onLoginValueChange(it) },
                    placeholder = { Text("Enter User Id") }
                )
            }
            Row(
                modifier = modifier.padding(0.dp, 25.dp, 0.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password",
                    Modifier.weight(0.29F, true),
                    fontFamily = FontFamily(Font(R.font.teko_semibold)),
                    fontSize = 24.sp,
                    maxLines = 1
                )
                OutlinedTextField(modifier = modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(customColors.secondary)
                    ),
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChange = { onPswdValChange(it) },
                    placeholder = { Text("Enter Password") }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onloginClick,
                    Modifier
                        .fillMaxWidth(0.5f)
                        .padding(0.dp, 50.dp, 0.dp, 0.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))

                ) {
                    Text("Login", color = Color.White)
                }
            }
            Row(
                modifier.padding(0.dp, 20.dp, 0.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onRegisterClick,
                    Modifier.fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))

                ) {
                    Text("Register", color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustAppTheme {

    }
}