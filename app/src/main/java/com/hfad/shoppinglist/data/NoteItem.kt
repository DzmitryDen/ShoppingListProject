package com.hfad.shoppinglist.data

data class NoteItem( // заметка
    val key: String = "",
    val title: String = "", // заголовок заметки
    val description: String = "", // текст заметки
    val timeInMillis: Long = 0L
)
