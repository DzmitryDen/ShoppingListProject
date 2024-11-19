package com.hfad.shoppinglist.shopping_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hfad.shoppinglist.dialog.MainDialog
import com.hfad.shoppinglist.ui.theme.EmptyText
import com.hfad.shoppinglist.ui.theme.GrayLight3
import com.hfad.shoppinglist.utils.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit // ф-ия принимает String (навигацию rout) и ничего не возвращает
) {
    val itemsList = viewModel.list.collectAsState(initial = emptyList())
    // получаем список из viewModel и вызываем collectAsState, т.к. мы работаем с Composable
    // и получаем список как состояние, чтобы при изменениях Composable обновлялся и перерисовывался список
    // initial = emptyList() значение по умолчанию

    LaunchedEffect(key1 = true) { // запускается единожды при отрисовке ShoppingListScreen
        viewModel.uiEvent.collect{uiEvent -> // у viewModel берем приемник uiEvent и вызываем слушатель collect (ждем событие)
            when(uiEvent) { // обработка события
                is UiEvent.Navigate -> {
                    onNavigate(uiEvent.route) // запускаем onNavigate и передаем route
                }
                else -> {}
            }
        }
    }

    LazyColumn( // отображаем список
        modifier = Modifier
            .fillMaxSize() // во весь экран
            .background(GrayLight3),
        contentPadding = PaddingValues(bottom = 144.dp) // отступ снизу (можем скроллить до
        // вытягивания последнего эл-та в зону видимости)
    ) {
        items(itemsList.value) { item ->  // в items передаем значение itemsList
            UiShoppingListItem(item) { event ->
                viewModel.onEvent(event)
            }
        }
    }

    MainDialog(dialogController = viewModel) // вызываем ф-ию MainDialog в кот. в DialogController передаем viewModel

    if (itemsList.value.isEmpty()) { // если список пустой, то отбразим текст
        Text(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(),
            text = "Empty",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = EmptyText
        )
    }
}