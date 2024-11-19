package com.hfad.shoppinglist.dialog

// события для Диалога
sealed class DilogEvent {
    // изменение текста
    data class OnTextChange(val  text: String): DilogEvent()

    // отмена
    object OnCancel: DilogEvent()

    // подтверждение
    object OnConfirm: DilogEvent()
}