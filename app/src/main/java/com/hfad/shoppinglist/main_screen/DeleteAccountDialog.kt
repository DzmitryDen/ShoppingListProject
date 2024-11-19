package com.hfad.shoppinglist.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.dialog.DialogController
import com.hfad.shoppinglist.dialog.DilogEvent
import com.hfad.shoppinglist.ui.theme.DarkText
import com.hfad.shoppinglist.ui.theme.GrayLight2
import com.hfad.shoppinglist.ui.theme.GrayLight4
import com.hfad.shoppinglist.ui.theme.Purple40

@Composable
fun DeleteAccountDialog(
    onDialogClose: () -> Unit,
    onConfirmDelete: (String, String) -> Unit
) {
    val email = remember {
        mutableStateOf("necodesarrollo@gmail.com")
    }
    val password = remember {
        mutableStateOf("123456789")
    }
    AlertDialog( // уже есть внутри Composable
        onDismissRequest = { // запрос на закрытие диалога

        },
        containerColor = GrayLight4, // фон Диалога
        title = null, // контейнер для заголовка (не будем использовать)
        text = { // в этом контейнере создадим текст для заголовка и текстовое поля для ввода текста
            Column( // т.к. будет несколько эл-тов
                modifier = Modifier // передаем Modifier
                    .fillMaxWidth() // по всей ширине
            ) {
                Text( // заголовок
                    text = "Delete account",
                    style = TextStyle(
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text( // заголовок
                    text = "Please, Sign In to delete this account!",
                    style = TextStyle(
                        color = DarkText,
                        fontSize = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(10.dp)) // Composable добавляет пространство (отступ от заголовка)
                // условие отображение поля для ввода текста
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { text ->
                        email.value = text
                    },
                    singleLine = true,
                    label = {
                        Text(stringResource(id = R.string.login))
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    visualTransformation = VisualTransformation.None,
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                )
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { text ->
                        password.value = text
                    },
                    singleLine = true,
                    label = {
                        Text(stringResource(id = R.string.password))
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    visualTransformation = VisualTransformation.None,
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        },
        confirmButton = { // контейнер кнопки подтверждения
            TextButton(onClick = {
                onConfirmDelete(email.value, password.value)
            }) {
                Text(
                    text = "OK", // текст кнопки
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },

        dismissButton = {
            TextButton(onClick = {
                onDialogClose()
            }) {
                Text(
                    text = "Cancel", // текст кнопки
                    style = TextStyle(
                        color = Purple40,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    )
}