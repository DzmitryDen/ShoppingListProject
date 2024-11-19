package com.hfad.shoppinglist.add_item_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.data.AddItem
import com.hfad.shoppinglist.ui.theme.GrayLight4
import com.hfad.shoppinglist.ui.theme.PurpleGrey
import com.hfad.shoppinglist.ui.theme.Red

@Composable
fun UiAddItem(
    item: AddItem, // в ф-ию передаем AddItem
    onEvent: (AddItemEvent) -> Unit // передаем ф-ию onEvent, кот. принмает событие AddItemEvent
// и ничего не возвращает
) {
    Card( // контейнер Card
        modifier = Modifier
            .fillMaxWidth() // контейнер Card занимает всю ширину
            .padding(top = 4.dp) // отступ сверху
            .clickable { // контейнер Card кликабельный
                onEvent(AddItemEvent.OnShowEditDialog(item)) // по нажатию вызываем ф-ию onEvent,
                // в кот. передаем событие AddItemEvent.OnShowEditDialog
            }
    ) {
        Row( // контейнер Row
            modifier = Modifier
                .fillMaxWidth() // контейнер Row занимает всю ширину
                .background(PurpleGrey),
            verticalAlignment = Alignment.CenterVertically // центрирование по вертикали
        ) {
            Text( // текстовый эл-т
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                text = item.name, // текст из item
                fontSize = 12.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )

            Checkbox( // чек-бокс
                checked = item.isCheck, // состояние из item
                onCheckedChange = { isChecked ->// ф-ия, кот. запускается при нажатии
                    onEvent(AddItemEvent.OnCheckedChange(item.copy(isCheck = isChecked))) // по нажатию вызываем ф-ию onEvent,
                    // в кот. передаем событие AddItemEvent.OnCheckedChange и перезаписываем
                    // состояние чек-бокса в item
                }
            )

            IconButton( // эл-т IconButton (удаление)
                onClick = { // слушатель нажатий
                    onEvent(AddItemEvent.OnDelete(item)) // по нажатию вызываем ф-ию onEvent,
                    // в кот. передаем событие AddItemEvent.OnDelete
                }
            ) {
                Icon( // иконка (картинка)
                    imageVector = Icons.Default.Delete, // иконка по умолчанию из Icons
                   // painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "Delete",
                    tint = Red
                )
            }
        }
    }
}