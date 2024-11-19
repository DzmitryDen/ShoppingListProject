package com.hfad.shoppinglist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hfad.shoppinglist.about_screen.AboutScreen
import com.hfad.shoppinglist.note_list_screen.NoteListScreen
import com.hfad.shoppinglist.settings_screen.SettingsScreen
import com.hfad.shoppinglist.shopping_list_screen.ShoppingListScreen
import com.hfad.shoppinglist.utils.Routes

@Composable
fun NavigationGraph(navController: NavHostController, onNavigate: (String) -> Unit) {

    NavHost(navController = navController, startDestination = Routes.SHOPPING_LIST) {
        composable(Routes.SHOPPING_LIST) {
            ShoppingListScreen() { route ->
                onNavigate(route)
            }
        }

        composable(Routes.NOTE_LIST) {
            NoteListScreen() { route ->
                onNavigate(route)
            }
        }

        composable(Routes.ABOUT) {
            AboutScreen()
        }

        composable(Routes.SETTINGS) {
            SettingsScreen()
        }
    }
}