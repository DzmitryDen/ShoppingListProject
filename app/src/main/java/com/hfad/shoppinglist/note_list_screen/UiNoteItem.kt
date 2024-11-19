package com.hfad.shoppinglist.note_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.data.NoteItem
import com.hfad.shoppinglist.ui.theme.LightText
import com.hfad.shoppinglist.ui.theme.Red
import com.hfad.shoppinglist.utils.Routes
import com.hfad.shoppinglist.utils.TimeUtils

//@Preview(showBackground = true)
@Composable
fun UiNoteItem(
    titleColor: String,
    item: NoteItem,
    onEvent: (NoteListEvent) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                top = 4.dp,
                end = 4.dp
            )
            .clickable { // слушатель нажатий (на весь эл-т - нажатие на всю заметку)
                onEvent(
                    NoteListEvent.OnItemClick(
                        Routes.NEW_NOTE
                                + "/${item.key}" +
                                "/${item.title}"
                                + "/${item.description}"
                                + "/${item.timeInMillis}" // передаем куда хотим перейти
                        // и через слэш id эл-та на кот. нажимаем
                    )
                )
            }
    ) {
        Column(Modifier.fillMaxWidth()) {

            Row(Modifier.fillMaxWidth()) {
                Text( // текст заголовка
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            start = 12.dp
                        )
                        .weight(1f),
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(android.graphics.Color.parseColor(titleColor)) // передаваемый цвет из Настроек
                )

                Text( // текст даты (время)
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            end = 12.dp
                        ),
                    text = TimeUtils.getCurrentTime(item.timeInMillis),
                    style = TextStyle(
                        color = LightText,
                        fontSize = 12.sp
                    )

                )
            }

            Row(Modifier.fillMaxWidth()) {
                Text( // текст (description)
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 12.dp,
                            bottom = 12.dp
                        )
                        .weight(1f),
                    text = item.description,
                    color = LightText,
                    maxLines = 2, // отображение в 2 строки
                    overflow = TextOverflow.Ellipsis // ... в конце последней строки
                )

                IconButton(
                    onClick = {
                        onEvent(NoteListEvent.OnShowDeleteDialog(item)) // вызываея ф-ию onEvent
                        // передаем вызов диалога и item для удаления
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = "Delete",
                        tint = Red
                    )
                }

            }
        }
    }
}