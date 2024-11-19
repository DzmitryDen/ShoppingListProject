package com.hfad.shoppinglist.new_note_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.data.NoteItem
import com.hfad.shoppinglist.ui.theme.BlueLight
import com.hfad.shoppinglist.ui.theme.BlueLight1
import com.hfad.shoppinglist.ui.theme.DarkText
import com.hfad.shoppinglist.ui.theme.GrayLight
import com.hfad.shoppinglist.utils.UiEvent


@SuppressLint("UnusedMaterialScaffoldPaddingParameter") // отключаем параметр padding в Scaffold
@Composable
fun NewNoteScreen(
    uid: String,
    noteItem: NoteItem?,
    viewModel: NewNoteViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit // при запусе ф-ии возвращаемся назад
) {
    val scaffoldState = rememberScaffoldState() // для сохранения состояния

    LaunchedEffect(key1 = true) {// запускается единожды при отрисовке NewNoteScreen
        viewModel.onEvent(NewNoteEvent.OnGetEditNote(noteItem)) // получаем заметку по ключу
        viewModel.uiEvent.collect { uiEvent -> // у viewModel берем приемник uiEvent и вызываем слушатель collect (ждем событие)
            when (uiEvent) { // проверяем и обрабатываем событие
                is UiEvent.PopBackStack -> { // возвращаемся назад
                    onPopBackStack()
                }

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GrayLight)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextField(
                            modifier = Modifier
                                .weight(1f),
                            value = viewModel.title,
                            onValueChange = { text -> // ф-ия изменения текста
                                viewModel.onEvent(NewNoteEvent.OnTitleChange(text))
                            },
                            label = {
                                Text(
                                    text = "Title...",
                                    fontSize = 16.sp
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = BlueLight
                            ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(
                                    android.graphics.Color.parseColor(viewModel.titleColor.value) // цвет из NewNoteViewModel
                                )
                            )
                        )

                        IconButton(
                            onClick = {
                                viewModel.onEvent(NewNoteEvent.OnSave(uid))
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.save),
                                contentDescription = "Save",
                                tint = BlueLight
                            )
                        }
                    }

                    TextField(
                        value = viewModel.description,
                        onValueChange = { text ->
                            viewModel.onEvent(NewNoteEvent.OnDescriptionChange(text))
                        },
                        label = {
                            Text(
                                text = "Description...",
                                fontSize = 16.sp
                            )
                        },

                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            color = DarkText
                        )
                    )

                }
            }
        }
    }
}