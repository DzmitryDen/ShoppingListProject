package com.hfad.shoppinglist.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hfad.shoppinglist.R

@Composable
fun UiColorItem( // в аргумент передаем ColorItem и ф-ию onEvent - обработчик событий
    item: ColorItem,
    onEvent: (SettingsEvent) -> Unit
) {
    IconButton(
        onClick = {
            onEvent(SettingsEvent.OnItemSelected(item.color)) // запускаем onEvent в него передаем событие
            // из SettingsEvent и передаем цвет на кот. нажали из item
        },
        modifier = Modifier
            .padding(start = 12.dp)
            .clip(CircleShape) // круглая форма
            .size(36.dp)
            .background(
                color = Color(android.graphics.Color.parseColor(item.color))// цвет получаем из ColorItem,
                //сначала цветовой код превращам из String в Integer, передаем в Color и получаем цвет
            )

    ) {
        if (item.isSelected) Icon( // иконку "галочку" показываем или нет в зависимости от условия выбора
            painter = painterResource(id = R.drawable.check),
            contentDescription = "Check",
            tint = Color.White
        )
    }
}