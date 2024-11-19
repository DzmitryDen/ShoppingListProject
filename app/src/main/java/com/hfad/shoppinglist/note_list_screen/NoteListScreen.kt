package com.hfad.shoppinglist.note_list_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hfad.shoppinglist.dialog.MainDialog
import com.hfad.shoppinglist.ui.theme.EmptyText
import com.hfad.shoppinglist.ui.theme.GrayLight3
import com.hfad.shoppinglist.ui.theme.GreenLight1
import com.hfad.shoppinglist.ui.theme.Red1
import com.hfad.shoppinglist.utils.UiEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = hiltViewModel(), // передаем класс NoteListViewModel (указываем hiltViewModel())
    onNavigate: (String) -> Unit // ф-ия принимает String (навигацию rout) и ничего не возвращает
) {
    val scaffoldState = rememberScaffoldState() // для сохранения состояния
//    val itemsList = viewModel.noteList.collectAsState(initial = emptyList())
    // получаем список из viewModel и вызываем collectAsState, т.к. мы работаем с Composable
    // и получаем список как состояние, чтобы при изменениях Composable обновлялся и перерисовывался список
    // initial = emptyList() значение по умолчанию

    LaunchedEffect(key1 = true) { // запускается единожды при отрисовке ShoppingListScreen
        viewModel.onEvent(NoteListEvent.OnGetNoteList)
        viewModel.uiEvent.collect { uiEvent -> // у viewModel берем приемник uiEvent и вызываем слушатель collect (ждем событие)
            when (uiEvent) { // обработка события
                is UiEvent.Navigate -> {
                    onNavigate(uiEvent.route) // запускаем onNavigate и передаем route
                }

                is UiEvent.ShowSnackBar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = uiEvent.message, // показываем Snackbar с сообщением
                        actionLabel = "Undone"     // кнопка Отмены действия
                    )



                    if (result == SnackbarResult.ActionPerformed) { // проверяем условие "Пользователь нажал кнопку Undone"
                        viewModel.onEvent(NoteListEvent.UnDoneDeleteItem) // запускаем событие Отмены удаления
                    }
                }

                else -> {}
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { // передаем для кастомизации SnackBar
            SnackbarHost(hostState = scaffoldState.snackbarHostState) { data ->
                Snackbar( // используем напрямую Composable Snackbar
                    snackbarData = data, // передаем data, кот. выдал новый SnackbarHost
                    backgroundColor = Red1,
                    actionColor = GreenLight1,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(bottom = 136.dp) // нижний отступ для SnackBar
                )
            }
        }
    ) {
        Column( // контейнер для поисковой строки и списка LazyColumn
            modifier = Modifier
                .fillMaxSize()
                .background(GrayLight3)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                TextField( // текстовое поле для поиска
                    value = viewModel.searchText, // получаем значение из viewModel
                    onValueChange = {text ->
                         viewModel.onEvent(NoteListEvent.OnTextSearchChange(text)) // передаем события при изменении текста
                    },
                    label = {
                        Text(text = "Search ...")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )
            }

            LazyColumn( // отображаем список
                modifier = Modifier
                    .fillMaxSize(), // во весь экран
//                    .background(GrayLight3)
                contentPadding = PaddingValues(bottom = 144.dp) // отступ снизу (можем скроллить до
                // вытягивания последнего эл-та в зону видимости)
            ) {
                items(viewModel.noteList) { item ->  // в items передаем значение itemsList
                    UiNoteItem(
                        viewModel.titleColor.value, // передаем цвет из oteListViewModel
                        item
                    ) { event ->
                        viewModel.onEvent(event)
                    }
                }
            }

            MainDialog(dialogController = viewModel) // вызываем ф-ию MainDialog в кот. в DialogController передаем viewModel

            if (viewModel.noteList.isEmpty()) { // если список пустой, то отбразим текст
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
}

