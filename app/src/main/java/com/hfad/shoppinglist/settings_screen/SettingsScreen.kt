package com.hfad.shoppinglist.settings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val list = viewModel.colorItemListState.value // список из SettingsViewModel

    Column( // контейнер
        modifier = Modifier
            .fillMaxSize() // контейнер во весь размер
            .padding(16.dp)
    ) {
        Text(
            text = "Title color",
            fontSize = 16.sp
        )
        Text(
            text = "Select a title color:",
            fontSize = 12.sp,
            color = Color.Gray
        )
        LazyRow( // список по горизонтали
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {

            items(list) { item ->
                UiColorItem(item = item) { event ->// отрисовываем элемент (кружок с цветом)
                    viewModel.onEvent(event)
                }
            }
        }
    }
}