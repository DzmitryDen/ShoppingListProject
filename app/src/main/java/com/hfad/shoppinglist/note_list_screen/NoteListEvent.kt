package com.hfad.shoppinglist.note_list_screen

import com.hfad.shoppinglist.data.NoteItem

// события для экрана NoteListScreen
sealed class NoteListEvent {

    // вызываем диалог удаления (передаем item для удаления - т.е. Заметку)
    data class OnShowDeleteDialog(val item: NoteItem) : NoteListEvent()

    // нажатие на эл-т (передаем route - название экрана, кот. будет открыт и идентификатор)
    data class OnItemClick(val route: String) : NoteListEvent()

    // ввод текста в Поиск
    data class OnTextSearchChange(val text: String) : NoteListEvent()

    // отмена удаления
    data object UnDoneDeleteItem: NoteListEvent()
    data object OnGetNoteList: NoteListEvent()
}