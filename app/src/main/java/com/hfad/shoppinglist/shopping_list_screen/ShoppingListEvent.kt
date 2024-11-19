package com.hfad.shoppinglist.shopping_list_screen

import com.hfad.shoppinglist.data.ShoppingListItem

// события для экрана ShoppingList
sealed class ShoppingListEvent {
    // вызываем диалог удаления (передаем item для удаления)
    data class OnShowDeleteDialog(val item: ShoppingListItem) : ShoppingListEvent()

    // вызываем диалог редактирования(передаем item для редактрования)
    data class OnShowEditDialog(val item: ShoppingListItem) : ShoppingListEvent()

    // нажатие на эл-т (передаем route - название экрана, кот. будет открыт)
    data class OnItemClick(val route: String) : ShoppingListEvent()

    // создание нового item (создаем объект, т.к. ничего передавать не нужно)
    // объект необходим только для распознания события
    object OnItemSave: ShoppingListEvent()
}