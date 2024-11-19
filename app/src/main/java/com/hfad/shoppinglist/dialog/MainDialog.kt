package com.hfad.shoppinglist.dialog


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.shoppinglist.ui.theme.DarkText
import com.hfad.shoppinglist.ui.theme.GrayLight2
import com.hfad.shoppinglist.ui.theme.GrayLight4
import com.hfad.shoppinglist.ui.theme.Purple40


@Composable
fun MainDialog(
    dialogController: DialogController
) {
    if (dialogController.openDialog.value) { // условие отображения диалога (true - отобаражаем)
        AlertDialog( // уже есть внутри Composable
            onDismissRequest = { // запрос на закрытие диалога
                dialogController.onDialogEvent(DilogEvent.OnCancel) // передаем событи OnCancel из DialogController
            },
            backgroundColor = GrayLight4, // фон Диалога
            title = null, // контейнер для заголовка (не будем использовать)
            text = { // в этом контейнере создадим текст для заголовка и текстовое поля для ввода текста
                Column( // т.к. будет несколько эл-тов
                    modifier = Modifier // передаем Modifier
                        .fillMaxWidth() // по всей ширине
                ) {
                    Text( // заголовок
                        text = dialogController.dialogTitle.value,
                        style = TextStyle(
                            color = DarkText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp)) // Composable добавляет пространство (отступ от заголовка)
                    if (dialogController.showEditableText.value) { // условие отображение поля для ввода текста
                        TextField( // поле для ввода текста
                            value = dialogController.editableText.value,
                            onValueChange = { text -> // ф-ия, кот. выдает текст каждый раз после изменения
                                dialogController.onDialogEvent(DilogEvent.OnTextChange(text)) // передаем событие
                            },
                            label = { // контейнер для текста-подсказки
                                Text(text = "List name:")
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = GrayLight2, // цвет фона для поля ввода
                                focusedIndicatorColor = Color.Transparent, // задаем цвет фокус индикатора (подчеркивание поля - прозрачный)
                                unfocusedIndicatorColor = Color.Transparent // задаем цвет анфокус индикатора (подчеркивание поля - прозрачный)
                            ),
                            shape = RoundedCornerShape(8.dp), // закругления углов поля ввода
                            singleLine = true, // ввод в одну строку
                            textStyle = TextStyle(
                                color = DarkText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            },
            confirmButton = { // контейнер кнопки подтверждения
                TextButton(onClick = {
                    dialogController.onDialogEvent(DilogEvent.OnConfirm) // в onClick отправляем событие OnConfirm
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
                    dialogController.onDialogEvent(DilogEvent.OnCancel) // в onClick отправляем событие OnCancel
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
}