package com.hfad.shoppinglist.add_item_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.dialog.MainDialog
import com.hfad.shoppinglist.ui.theme.BlueLight
import com.hfad.shoppinglist.ui.theme.BlueLight1
import com.hfad.shoppinglist.ui.theme.DarkText
import com.hfad.shoppinglist.ui.theme.EmptyText
import com.hfad.shoppinglist.ui.theme.GrayLight3
import com.hfad.shoppinglist.utils.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter") // отключаем параметр padding в Scaffold
@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel = hiltViewModel() // инициализируем класс AddItemViewModel
// (инициализировать будет Hilt)
) {

    val scaffoldState = rememberScaffoldState() // для сохранения состояния
    val itemsList = viewModel.itemsList?.collectAsState(initial = emptyList()) // список товаров,
    // берем из viewModel.itemsList (это Flow), вызываем .collectAsState (значение по умолчанию - пустой список)

    LaunchedEffect(key1 = true) {// запускается единожды при отрисовке AddItemScreen

        viewModel.uiEvent.collect { uiEvent -> // у viewModel берем приемник uiEvent и вызываем слушатель collect (ждем событие)
            when (uiEvent) { // обработка события
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar( // показываем Snackbar с сообщением
                        uiEvent.message
                    )

                }

                else -> {}
            }
        }
    }

    Scaffold( // используем контейнер т.к. будем отображать снэкбар
        scaffoldState = scaffoldState,
        snackbarHost = { // передаем для кастомизации SnackBar
            SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                Snackbar(
                    // используем напрямую Composable Snackbar
                    snackbarData = data, // передаем data, кот. выдал новый SnackbarHost
                    backgroundColor = BlueLight1,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    ) {
// используем контейнер т.к. будем отображать снэкбар
        Column( // Контейнер Column
            modifier = Modifier
                .fillMaxSize()
                .background(GrayLight3)
        ) {

            Card( // Контейнер Card
                modifier = Modifier
                    .fillMaxWidth() // Card занимает всю ширину
                    .padding(8.dp) // отступ по всем сторонам
            ) {
                Row( // Контейнер Row
                    modifier = Modifier
                        .fillMaxWidth(), // Row занимает всю ширину
                    verticalAlignment = Alignment.CenterVertically // выравнивание по центру по вертикали
                ) {
                    TextField( // поле для ввода текста
                        modifier = Modifier
                            .weight(1f), // вес
                        value = viewModel.itemText.value, // значение из viewModel
                        onValueChange = { text -> // обновляем то что пишем
                            viewModel.onEvent(AddItemEvent.OnTextChange(text)) // передаем во viewModel
                            // событие AddItemEvent.OnTextChange
                        },
                        label = { // подсказка
                            Text(
                                text = "New item",
                                fontSize = 12.sp
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors( // цвета
                            backgroundColor = Color.White, // фон
                            focusedIndicatorColor = BlueLight, // цвет линии индикатора фокуса
                            unfocusedIndicatorColor = Color.Transparent // прозрачный если не в фокусе
                        ),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = DarkText
                        ),
                        singleLine = true // ввод текста в одну строку
                    )

                    IconButton( // кнопка для добавления товара
                        onClick = { // слушатель нажатий
                            viewModel.onEvent(AddItemEvent.OnItemSave) // добавляем эл-т в список
                            // передаем во viewModel событие AddItemEvent.OnItemSave
                        }
                    ) {
                        Icon( // иконка кнопки
                            painter = painterResource(id = R.drawable.add_icon),
                            contentDescription = "Add" // название кнопки
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 4.dp,
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                if (itemsList != null) { // проверка - если itemsList не null
                    items(itemsList.value) { item ->  // для отрисовки эл-тов UiAddItem
                        UiAddItem(item = item, onEvent = { event ->
                            viewModel.onEvent(event) // передаем во viewModel событие .onEvent(event)
                        })
                    }
                }
            }
        }
        MainDialog(dialogController = viewModel) // диалог

        if (itemsList?.value?.isEmpty() == true) { // если список пустой, то отбразим текст
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
}