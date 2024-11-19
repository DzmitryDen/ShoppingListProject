package com.hfad.shoppinglist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.shoppinglist.add_item_screen.AddItemScreen
import com.hfad.shoppinglist.data.NoteItem
import com.hfad.shoppinglist.login_screen.data.UserData
import com.hfad.shoppinglist.login_screen.ui.LoginScreen
import com.hfad.shoppinglist.main_screen.MainScreen
import com.hfad.shoppinglist.new_note_screen.NewNoteScreen
import com.hfad.shoppinglist.utils.Routes

@Composable
fun MainNavigationGraph() {

    val navController = rememberNavController() // инициализация navController

    NavHost(navController = navController, startDestination = Routes.LOGIN_SCREEN) {
        composable(Routes.ADD_ITEM + "/{listId}") { // чз слэш
            // в фигурных скобках передаем название ключа под которым будем получать данный аргумент
            AddItemScreen()
        }

        composable(Routes.NEW_NOTE + "/{key}/{title}/{description}/{time}/{uid}") { backStackEntry ->
            // в фигурных скобках передаем название ключа под которым будем получать данный аргумент
            val key = backStackEntry.arguments?.getString("key")
            val uid = backStackEntry.arguments?.getString("uid")
            val title = backStackEntry.arguments?.getString("title")
            val description = backStackEntry.arguments?.getString("description")
            val timeString = backStackEntry.arguments?.getString("time")
            val timeLong = if (timeString?.isNotEmpty() == true) {
                timeString.toLong()
            } else {
                0L
            }
            val noteItem = if (key?.isNotEmpty() == true) {
                NoteItem(key, title!!, description!!, timeLong)
            } else {
                null
            }
            if (uid != null && key != null) {
                NewNoteScreen(uid, noteItem) {
                    navController.popBackStack() // возвращаемся на предыдущий Composable
                }
            }
        }

        composable(Routes.MAIN_SCREEN + "/{email}/{uid}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val uid = backStackEntry.arguments?.getString("uid")
            val userData = UserData(email!!, uid!!)
            MainScreen(userData, navController)
        }
        composable(Routes.LOGIN_SCREEN) {
            LoginScreen { userData ->
                navController.navigate(
                    Routes.MAIN_SCREEN
                            + "/${userData.email}" +
                            "/${userData.uid}"
                )
            }
        }
    }
}