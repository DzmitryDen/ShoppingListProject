package com.hfad.shoppinglist.new_note_screen

import com.hfad.shoppinglist.data.NoteItem

sealed class NewNoteEvent {
    data class OnTitleChange(val title: String) : NewNoteEvent() // обновление текста в title
    data class OnDescriptionChange(val description: String) :
        NewNoteEvent() // обновление текста в description

    data class OnSave(val uid: String) : NewNoteEvent() // сохранение заметки
    data class OnGetEditNote(val noteItem: NoteItem?) : NewNoteEvent()
}