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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.animesh.justapp.data.User
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.viewmodels.LoginViweModel

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

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Login", Modifier.weight(0.3F, true))
                var id = remember { mutableStateOf("") }
                loginViewModel.username = id.value
                TextField(
                    value = id.value,
                    onValueChange = { newid -> id.value = newid },
                    placeholder = { Text("Enter id") }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Password", Modifier.weight(0.3F, true))
                var pswd = remember { mutableStateOf("") }
                loginViewModel.password = pswd.value
                TextField(
                    value = pswd.value,
                    onValueChange = { newpswd -> pswd.value = newpswd },
                    placeholder = { Text("Enter pswd") }
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

                    // Modifier.weight(0.5F,true),
                    Modifier.fillMaxWidth(0.5f),
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustAppTheme {
        SetUpLoginRegisterScreen(loginViewModel)
    }
}