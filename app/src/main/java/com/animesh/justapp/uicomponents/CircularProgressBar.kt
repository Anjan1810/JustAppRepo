package com.animesh.justapp.uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBar() {

       //Box(modifier = Modifier){
           Row(
               modifier = Modifier
                   .fillMaxWidth()
               ,
               horizontalArrangement = Arrangement.Center,
               verticalAlignment = Alignment.CenterVertically
           ) {
               CircularProgressIndicator(progress = 0.9f, modifier = Modifier.size(70.dp), color = customColors.onSecondary, strokeWidth = 5.dp)

           }
      // }


}
@Composable
fun brushForTextField(color:Color): Brush {
    val brush = Brush.verticalGradient(
        0.0f to Color.White,
        0.3f to color,
        1.0f to Color.White,

        startY = 0.0f,
        endY = 100.0f
    )
    return brush
}
val JustAppGreen = Color(0xFF1DB954)
val JustAppBlack = Color(0xFF191414)
val JustAppGray = Color(0xFFB3B3B3)
val JustAppyellow = Color(0xFFFFD180)
val JustAppdarkteal = Color(0xFF00555B)
val JustAppSoftGreen = Color(0xFFA0E9A7)
val JustAppMutedPink = Color(0xFFFFC5D9)
val JustAppBrightBlue = Color(0xFF0088FF)

// Define your custom color palette
val colors = lightColors(
    primary = JustAppGreen,
    onPrimary = JustAppBlack,
    secondary = JustAppGray,
    onSecondary = JustAppBlack,
)
val fontcolors = lightColors(
    primary = JustAppdarkteal,
    onPrimary = JustAppBlack,
    secondary = JustAppGray,
    onSecondary = JustAppBlack,
)
val customColors = lightColors(
    primary = JustAppyellow,
    onPrimary = JustAppSoftGreen,
    secondary = JustAppMutedPink,
    onSecondary = JustAppBrightBlue,
)

