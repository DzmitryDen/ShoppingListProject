package com.hfad.shoppinglist.add_item_screen

import com.hfad.shoppinglist.data.AddItem

// события для экрана AddItemScreen
sealed class AddItemEvent {
    data class OnDelete(val item: AddItem) : AddItemEvent() // удаление
    data class OnShowEditDialog(val item: AddItem) : AddItemEvent() // диалог редактирования
    data class OnTextChange(val text: String) : AddItemEvent() // изменение текста
    data class OnCheckedChange(val item: AddItem) : AddItemEvent() // чек-бокс

    // создание нового item (создаем объект, т.к. ничего передавать не нужно)
    // объект необходим только для распознания события
    object OnItemSave : AddItemEvent()
}