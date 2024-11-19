package com.hfad.shoppinglist.dialog

import androidx.compose.runtime.MutableState

interface DialogController {
    val dialogTitle: MutableState<String> // для изменения (сохранения) заголовка (MutableState т.к.
    // Composable будет изменяться при изменении состояния)

    val editableText: MutableState<String> // для изменения (сохранения) вводимого текста
    val openDialog: MutableState<Boolean>  // для отображения (скрытия) диалога
    val showEditableText: MutableState<Boolean>  // для отображения (скрытия) поля для ввода текста
    fun onDialogEvent(event: DilogEvent) // управление диалогом
}