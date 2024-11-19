package com.hfad.shoppinglist.main_screen

// события для экрана MainScreen
sealed class MainScreenEvent {
    // вызываем диалог для создания нового эл-та списка
//    object OnShowEditDialog : MainScreenEvent()

    // объект необходим только для распознания события
    object OnItemSave : MainScreenEvent()
    object OnSignOut : MainScreenEvent()

    data class Navigate(val route: String) : MainScreenEvent() // навигация
    data class DeleteAccount(val email: String, val password: String) :
        MainScreenEvent() // удаление аккаунта

    data class NavigateMain(val route: String) : MainScreenEvent() // навигация
    data class OnNewItemClick(val route: String, val uid: String) :
        MainScreenEvent() // добавление нового эл-та

}