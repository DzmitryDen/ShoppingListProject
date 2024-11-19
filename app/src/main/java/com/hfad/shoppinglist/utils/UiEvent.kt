package com.hfad.shoppinglist.utils

// событие для любого экрана
sealed class UiEvent {
    // возврат на предыдущий экран
    object PopBackStack : UiEvent()

    // переключение м-у экранами
    data class Navigate(val route: String) : UiEvent()

    // событие, чтобы отличить навигацию по другим экранам
    data class NavigateMain(val route: String) : UiEvent()

    // показать SnackBar
    data class ShowSnackBar(val message: String) : UiEvent()
}