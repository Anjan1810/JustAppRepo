package com.animesh.justapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.animesh.justapp.ui.theme.JustAppTheme
import com.animesh.justapp.uicomponents.customColors
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.animesh.justapp.repository.DataStoreManager
import kotlinx.coroutines.launch

class SettingsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = customColors.primary
                ) {
                    SettingsScreenView()
                }
            }
        }
    }
}

@Composable
fun SettingsScreenView() {
    val context = LocalContext.current
    var selectedTheme by remember { mutableStateOf(0) }
    var saveSettings by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var dataStoreManager = DataStoreManager(LocalContext.current)

    if (saveSettings) {
        LaunchedEffect(Unit) {
            launch {
                dataStoreManager.saveCurrency(selectedCurrency.toString())
                 dataStoreManager.saveTheme( selectedTheme.toString())
            }
        }
        context.startActivity(Intent(context, HomeScreenActivity::class.java))
    }

    val themes = listOf(
        "Light Theme",
        "Dark Theme"
    )

    val currencies = listOf(
        "INR",
        "USD",
        "EUR",
        "GBP",
        "JPY"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }, navigationIcon = {
                    IconButton(
                        onClick = {
                           context.startActivity(
                                Intent(
                                    context, HomeScreenActivity::class.java
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Select Background Theme",
                    style = MaterialTheme.typography.h6
                )
                Spacer(Modifier.height(8.dp))
                themes.forEachIndexed { index, theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedTheme = index
                            }
                    ) {
                        RadioButton(
                            selected = selectedTheme == index,
                            onClick = { selectedTheme = index },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.secondary)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = theme, style = MaterialTheme.typography.body1)
                    }
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "Select Currency",
                    style = MaterialTheme.typography.h6
                )
                Spacer(Modifier.height(8.dp))
                currencies.forEachIndexed { index, currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedCurrency = currency
                            }
                    ) {
                        RadioButton(
                            selected = selectedCurrency == currency,
                            onClick = { selectedCurrency = currency },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.secondary)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = currency, style = MaterialTheme.typography.body1)
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    saveSettings = !saveSettings
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    JustAppTheme {
        Greeting("Android")
    }
}