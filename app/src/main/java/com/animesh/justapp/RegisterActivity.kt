package com.animesh.justapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animesh.justapp.data.User
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.CircularProgressBar
import com.animesh.justapp.uicomponents.brushForTextField
import com.animesh.justapp.uicomponents.colors
import com.animesh.justapp.uicomponents.customColors
import com.animesh.justapp.viewmodels.RegisterViewModel

class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SetUpRegisterScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun SetUpRegisterScreen(viewModel: RegisterViewModel) {
    val context = LocalContext.current
    var isDisplayed = remember { mutableStateOf(false) }

    if (isDisplayed.value) {
        CircularProgressBar()
        val result = viewModel.register(
            User(
                viewModel.username,
                viewModel.email,
                viewModel.password
            )
        )
        isDisplayed.value = false
        context.startActivity(
            Intent(
                context, MainActivity::class.java
            )
        )
    }

    Box(modifier = Modifier.background(customColors.onPrimary)) {

        Column(
            modifier = Modifier
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
                    text = "Name", Modifier.weight(0.29F, true),
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 20.sp
                )
                var id = remember { mutableStateOf("") }
                viewModel.username = id.value
                OutlinedTextField(modifier = Modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(customColors.primary)
                    ),
                    value = id.value,
                    onValueChange = { newid -> id.value = newid },
                    placeholder = { Text("Enter USERNAME") }
                )
            }
            Row(
                modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EmailId", Modifier.weight(0.29F, true),
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 20.sp,
                    maxLines = 1
                )
                var emailId = remember { mutableStateOf("") }
                viewModel.email = emailId.value
                OutlinedTextField(modifier = Modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(customColors.primary)
                    ),
                    value = emailId.value,
                    onValueChange = { newemailId -> emailId.value = newemailId },
                    placeholder = { Text("Enter emailId") }
                )
            }
            Row(
                modifier = Modifier.padding(0.dp, 25.dp, 0.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password", Modifier.weight(0.29F, true),
                    fontFamily = FontFamily(Font(R.font.handlee_regular)),
                    fontSize = 20.sp,
                    maxLines = 1
                )
                var pswd = remember { mutableStateOf("") }
                viewModel.password = pswd.value
                OutlinedTextField(modifier = Modifier
                    .weight(0.75f, true)
                    .background(
                        brushForTextField(customColors.primary)
                    ),
                    value = pswd.value,
                    onValueChange = { newpswd -> pswd.value = newpswd },
                    placeholder = { Text("Enter pswd") }
                )
            }
            Row(
                modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(

                    onClick = {
                        if (viewModel.username.isNullOrEmpty() || viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrEmpty()) {
                            Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            isDisplayed.value = true

                        }


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
fun DefaultPreview2() {
    JustAppTheme {

    }
}