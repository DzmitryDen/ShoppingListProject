package com.hfad.shoppinglist.shopping_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.data.ShoppingListItem
import com.hfad.shoppinglist.settings_screen.ColorUtils
import com.hfad.shoppinglist.ui.theme.BlueLight
import com.hfad.shoppinglist.ui.theme.DarkText
import com.hfad.shoppinglist.ui.theme.GreenLight
import com.hfad.shoppinglist.ui.theme.LightText
import com.hfad.shoppinglist.ui.theme.Red
import com.hfad.shoppinglist.utils.ProgressHelper
import com.hfad.shoppinglist.utils.Routes

@Composable
fun UiShoppingListItem(
    item: ShoppingListItem, // передаем ShoppingListItem
    onEvent: (ShoppingListEvent) -> Unit // передаем ф-ию onEvent, кот. принмает событие ShoppingListEvent
// и ничего не возвращает
) {

   val progress = ProgressHelper.getProgress( // ф-ия для вычисления прогресса
        item.allItemsCount, // общее кол-во эл-тов (товаров)
        item.allSelectedItemsCount // отмеченное кол-во эл-тов (товаров)
    )

    ConstraintLayout(
        modifier = Modifier.padding(
            start = 4.dp, top = 20.dp, end = 4.dp // задаем отступы
        )
    ) {
        val (card, deleteButton, editButton, counter) = createRefs() // референс (т.о. Сonstraintlayout знает, что есть эл-ты
        // и их к чему-то нужно добавить)
        Card(
            modifier = Modifier
                .fillMaxWidth()       // отображаем на всю ширину
                .constrainAs(card) {// передаем ссылку (референс) на компонент
                    top.linkTo(parent.top) // прикрепляем верх к верху родителя
                    start.linkTo(parent.start) // левую сторону к левой родителя
                    end.linkTo(parent.end) // правую к правой родителя
                }
                .clickable { // слушатель нажатий
                    onEvent(ShoppingListEvent.OnItemClick(// запускается onEvent и передаем событие
                        Routes.ADD_ITEM + "/${item.id}" // чз слэш добавляем id эл-та на кот. нажимаем
                    ))
                },
            colors = CardDefaults.cardColors( // задаем цвет карточки
                containerColor = Color.White
            )

        ) {
            Column( // для расположения эл-тов по вертикали
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(  // текст для заголовка
                    text = item.name,
                    style = TextStyle(
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Text( // текст для даты
                    text = item.time,
                    style = TextStyle(
                        color = LightText,
                        fontSize = 12.sp
                    )
                )

                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    progress =  progress,
                    color = ColorUtils.getProgressColor(progress) // цвет индикатора
                )
            }
        }

        IconButton(
            onClick = {
                onEvent(ShoppingListEvent.OnShowDeleteDialog(item)) // в параметре onEvent передаем Диалог для Удаления
                // и передаем item, кот. хотим удалить
            },
            modifier = Modifier
                .constrainAs(deleteButton) {// передаем ссылку (референс) на компонент
                    top.linkTo(card.top) // прикрепляем верх к верху card
                    bottom.linkTo(card.top) // низ к верху card
                    end.linkTo(card.end) // правую сторону к правой card
                }
                .padding(end = 12.dp)
                .size(30.dp) // размер кнопки
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_icon), // добавили иконку
                contentDescription = "Delete",
                modifier = Modifier
                    .clip(CircleShape) // добавляем круклый фон
                    .background(Red) // фон
                    .padding(4.dp),
                tint = Color.White // цвет иконки
            )
        }

        IconButton(
            onClick = {
                onEvent(ShoppingListEvent.OnShowEditDialog(item)) // в параметре onEvent передаем Диалог для Редактирования
                // и передаем item, кот. хотим редактировать
            },
            modifier = Modifier
                .constrainAs(editButton) {// передаем ссылку (референс) на компонент
                    top.linkTo(card.top) // прикрепляем верх к верху card
                    bottom.linkTo(card.top) // низ к верху card
                    end.linkTo(deleteButton.start) // правую сторону к левой deleteButton
                }
                .padding(end = 4.dp)
                .size(30.dp) // размер кнопки
        ) {
            Icon(
                painter = painterResource(id = R.drawable.edit_icon), // добавили иконку
                contentDescription = "Edit",
                modifier = Modifier
                    .clip(CircleShape) // добавляем круклый фон
                    .background(GreenLight) // фон
                    .padding(4.dp),
                tint = Color.White // цвет иконки
            )
        }

        Card(
            shape = RoundedCornerShape(4.dp), // закругляения углов
            modifier = Modifier
                .constrainAs(counter) {// передаем ссылку (референс) на компонент
                    top.linkTo(card.top) // прикрепляем верх к верху card
                    bottom.linkTo(card.top) // низ к верху card
                    end.linkTo(editButton.start) // правую сторону к левой editButton
                }
                .padding(end = 4.dp)
        ) {
            Text(    // счетчик
                text = "${item.allItemsCount}/${item.allSelectedItemsCount}",
                modifier = Modifier
                    .background(BlueLight)
                    .padding(4.dp),
                style = TextStyle(
//                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            )

        }
    }
}