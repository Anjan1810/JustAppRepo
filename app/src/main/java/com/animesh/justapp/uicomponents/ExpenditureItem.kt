package com.animesh.justapp.uicomponents

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.animesh.justapp.uicomponents.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.android.style.TextDecorationSpan
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.animesh.justapp.data.Expenditure
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenditureItem(
    expenditure: Expenditure,
    fontFamily: FontFamily,
    onDelete: (Expenditure) -> Unit
) {

    Row(
        modifier = Modifier
            .border(1.dp, Color.Magenta, shape = RoundedCornerShape(15.dp))
            .clickable { }
            .heightIn(150.dp, 400.dp)
            .background(
                brush = Brush.verticalGradient(
                    //listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
                    listOf(Color(0xFF26A69A), Color(0xFFFFA726))
                ), shape = RoundedCornerShape(15.dp)
            )
            .padding(5.dp)
            , verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .padding(2.dp)
                .weight(0.5f, true), verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Expense On",
                modifier = Modifier
                    .padding(2.dp, 2.dp, 0.dp, 0.dp),
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontFamily = fontFamily, textDecoration = TextDecoration.Underline
            )
            Text(
                text = expenditure.expenditureName,
                modifier = Modifier
                    .padding(2.dp),
                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        Column(
            modifier = Modifier
                .padding(2.dp)
                .weight(0.25f, true),verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Cost",
                modifier = Modifier
                    .padding(2.dp, 2.dp, 0.dp, 0.dp),
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontFamily = fontFamily,textDecoration = TextDecoration.Underline
            )
            Text(
                text = expenditure.expenditureAmount,
                modifier = Modifier
                    .padding(2.dp),

                color = Color.Black,
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif
            )
        }


        Image(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete",
            modifier = Modifier
                .weight(0.15f, true)
                .clickable { onDelete(expenditure) }, alignment = Alignment.Center
        )

    }



}