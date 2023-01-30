package com.animesh.justapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animesh.justapp.data.User
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.CircularProgressBar
import com.animesh.justapp.uicomponents.brushForTextField
import com.animesh.justapp.viewmodels.LoginViweModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViweModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SetUpLoginRegisterScreen(loginViewModel)
                }
            }
        }
    }
}

@Composable
fun SetUpLoginRegisterScreen(loginViewModel: LoginViweModel) {
    val context = LocalContext.current
    Box(modifier = Modifier.background(Color.White)) {

        Column(
            modifier = Modifier.padding(25.dp, 100.dp, 25.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "UserId",
                    Modifier.weight(0.29F, true),
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp
                )
                var id = remember { mutableStateOf("") }
                loginViewModel.username = id.value
                TextField(modifier = Modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(Color.Green)
                    ),
                    value = id.value,
                    onValueChange = { newid -> id.value = newid },
                    placeholder = { Text("Enter UserId") }
                )
            }
            Row(modifier = Modifier.padding(0.dp,25.dp,0.dp,0.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Password",
                    Modifier.weight(0.29F, true),
                    fontFamily = FontFamily.Cursive,
                    fontSize = 24.sp,
                    maxLines = 1
                )
                var pswd = remember { mutableStateOf("") }
                loginViewModel.password = pswd.value
                TextField(modifier = Modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(Color.Green)
                    ),
                    value = pswd.value,
                    onValueChange = { newpswd -> pswd.value = newpswd },
                    placeholder = { Text("Enter Password") }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val result = loginViewModel.doLogin(
                            User(
                                loginViewModel.username,
                                "",
                                loginViewModel.password
                            )
                        )
                        if (result != null && result.toString().equals("OK")) {
                            context.startActivity(
                                Intent(
                                    context,
                                    HomeScreenActivity::class.java
                                )
                            )
                        }
                    },
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(

                    onClick = {
                        context.startActivity(Intent(context, RegisterActivity::class.java))
                    },

                    Modifier.fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))

                ) {
                    Text("Register", color = Color.White)
                }
            }
            CircularProgressBar(isDisplayed = loginViewModel.loadingProgressBar.value)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustAppTheme {

    }
}