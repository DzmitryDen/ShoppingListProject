package com.hfad.shoppinglist.main_screen

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.hfad.shoppinglist.ui.theme.GrayLight
import com.hfad.shoppinglist.ui.theme.Purple40

@Composable
fun BottomNav(
    currentRoute: String?, // передаем currentRoute
    onNavigate: (String) -> Unit, // передаем ф-ию для запуска навигации
    onAccountClick: () -> Unit
) {

    val listItems = listOf( // создаем список элементов
        BottomNavItem.Account,
        BottomNavItem.ListItem,
        BottomNavItem.NoteItem,
        BottomNavItem.SettingsItem
    )

    BottomNavigation(backgroundColor = Color.White) {

        listItems.forEach { bottomNavItem -> // проходим по списку эл-тов
            BottomNavigationItem(            // отрисовывает (создает) item
                selected = currentRoute == bottomNavItem.route, // отмечает выбранный эл-т (сравниваем отрисовываемый экран с выбранным)
                onClick = {                  // слушатель нажатий
                    if (bottomNavItem.route.isEmpty()) {
                        onAccountClick()
                    } else {
                        onNavigate(bottomNavItem.route)
                    } //  для навигации при нажатии вызываем ф-ию onNavigate
                },
                icon = {                     // иконка (картинка)
                    Icon(
                        painter = painterResource(id = bottomNavItem.iconId),
                        contentDescription = "icon"
                    )
                },
                label = {                    // текст
                    Text(text = bottomNavItem.title)
                },
                selectedContentColor = Purple40, // цвет выбранного эл-та
                unselectedContentColor = GrayLight, // цвет не выбранного эл-та
                alwaysShowLabel = false // текст будет показан только на выбранном эл-те
            )
        }
    }
}