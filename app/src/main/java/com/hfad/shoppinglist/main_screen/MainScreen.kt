package com.hfad.shoppinglist.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hfad.shoppinglist.R
import com.hfad.shoppinglist.dialog.MainDialog
import com.hfad.shoppinglist.login_screen.data.UserData
import com.hfad.shoppinglist.main_screen.drawer_menu.ui.DrawerMenu
import com.hfad.shoppinglist.navigation.NavigationGraph
import com.hfad.shoppinglist.utils.Routes
import com.hfad.shoppinglist.utils.UiEvent
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    user: UserData,
    mainNavHostController: NavHostController,
    viewModel: MainScreenViewModel = hiltViewModel() // т.к. class MainScreenViewMode помечен анотацией @HiltViewModel
) {
    val showDeleteAccountDialog = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val drawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed) // инициализация drawerState

    val navController = rememberNavController() // инициализация navController

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route // экран выбранный в данный момент (путь)

    LaunchedEffect(key1 = true) {// запускается единожды при отрисовке ShoppingListScreen
        viewModel.uiEvent.collect { uiEvent -> // у viewModel берем приемник uiEvent и вызываем слушатель collect (ждем событие)
            when (uiEvent) { // обработка события (при разных условиях запускаются разные навигации)
                is UiEvent.NavigateMain -> { //
                    mainNavHostController.navigate(uiEvent.route) // используем mainNavHostController
                }

                is UiEvent.Navigate -> {
                    navController.navigate(uiEvent.route) // используем navController для BottomNavigation
                }

                else -> {}
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(userData = user) { index ->
                coroutineScope.launch {
                    drawerState.close()
                    when (index) {
                        0 -> {
                            viewModel.onEvent(MainScreenEvent.Navigate(Routes.ABOUT))
                        }

                        1 -> {
                            showDeleteAccountDialog.value = true
                        }

                        2 -> {
                            viewModel.onEvent(MainScreenEvent.OnSignOut)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNav(
                    currentRoute,
                    onNavigate = { route ->
                        viewModel.onEvent(MainScreenEvent.Navigate(route)) // передаем событие Navigate и указывам route
                    }

                ) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            floatingActionButton = {
                if (viewModel.showFloatingButton.value) {
                    Box() {
                        FloatingActionButton( // if - условие отображения кнопки
                            modifier = Modifier
                                .offset(y = 40.dp),
                            onClick = {
                                viewModel.onEvent(
                                    MainScreenEvent.OnNewItemClick(
                                        currentRoute ?: Routes.SHOPPING_LIST,
                                        user.uid
                                    )
                                ) // добавление нового эл-та
                            },
                            containerColor = Color(0xFF6650a4)   // меняем цвет кнопки на требуемый
                        ) {
                            Icon(                                     // добавили картинку для кнопки
                                painter = painterResource(
                                    id = R.drawable.add_icon
                                ),
                                contentDescription = "Add",
                                tint = Color.White                    // цвет иконки
                            )
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center

        ) {
            NavigationGraph(navController) { rout ->
                viewModel.onEvent(MainScreenEvent.NavigateMain(rout)) // передаем событие NavigateMain и указывам route
            }
            MainDialog(dialogController = viewModel) // вызываем ф-ию MainDialog в кот. в DialogController передаем viewModel
            if (showDeleteAccountDialog.value) {
                DeleteAccountDialog(
                    onDialogClose = {
                        showDeleteAccountDialog.value = false
                    },
                    onConfirmDelete = { email, password ->
                        viewModel.onEvent(
                            MainScreenEvent.DeleteAccount(
                                email,
                                password
                            )
                        )
                    }
                )
            }
        }
    }
}