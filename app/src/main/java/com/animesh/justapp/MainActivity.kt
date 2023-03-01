package com.animesh.justapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.animesh.justapp.data.MenuItem
import com.animesh.justapp.data.User
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.*
import com.animesh.justapp.viewmodels.LoginViweModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    StatefulLoginRegisterScreen()
                }
            }
        }
    }
}

@Composable
fun StatefulLoginRegisterScreen(loginViewModel: LoginViweModel = viewModel()) {
    val context = LocalContext.current

    StatelessLoginRegisterScreen(
        id = loginViewModel.username.value,
        password = loginViewModel.password.value,
        onloginClick = {
            val result = loginViewModel.doLogin(
                User(
                    loginViewModel.username.value,
                    "", loginViewModel.password.value
                )
            )
            if (result != null && result.toString().equals("OK")) {
                context.startActivity(
                    Intent(
                        context, HomeScreenActivity::class.java
                    )
                )
            }
        },
        onRegisterClick = { context.startActivity(Intent(context, RegisterActivity::class.java)) },
        onLoginValueChange = { newid -> loginViewModel.username.value= newid },
        onPswdValChange = { newpswd -> loginViewModel.password.value = newpswd },
        modifier = Modifier
    )


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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "UserId",
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
                    placeholder = { Text("Enter UserId") }
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
            Row(modifier.padding(0.dp,0.dp,0.dp,10.dp),
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