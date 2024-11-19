package com.hfad.shoppinglist.about_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.ui.theme.BlueLight1


@Preview(showBackground = true)
@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current // для открытия ссылки по клику
    Column(
        modifier = Modifier
            .fillMaxSize(), // контейнер Column занимает весь экран
        horizontalAlignment = Alignment.CenterHorizontally, // выравниване  по горизонтали
        verticalArrangement = Arrangement.Center // выравниване  по центру
    ) {
        Icon(


            painter = painterResource(id = R.drawable.blob_logo), // добавляем иконку логотипа
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp),
            tint = BlueLight1
        )
        Spacer(modifier = Modifier.height(12.dp)) // отступ
        Text(
            text = "This app developed by DENISEVICH D. \n " +
                    "Version - 1.0.0 \n" +
                    "To get more information:\n",
            textAlign = TextAlign.Center,
//
        )
        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable {
                    uriHandler.openUri("http://denisevich-test.by") // по клику переход по ссылке
                },
            text = ">>> Click here <<<",
            color = BlueLight1
        )
    }
}