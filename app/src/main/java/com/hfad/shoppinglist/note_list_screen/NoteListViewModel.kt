package com.hfad.shoppinglist.note_list_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.shoppinglist.data.NoteItem
import com.hfad.shoppinglist.datastore.DataStoreManager
import com.hfad.shoppinglist.dialog.DialogController
import com.hfad.shoppinglist.dialog.DilogEvent
import com.hfad.shoppinglist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    dataStoreManager: DataStoreManager     // передаем DataStoreManager (для получения цвета из Настроек)
) : ViewModel(),
    DialogController { // наследуемся от ViewModel, подключаем interface DialogController

    var noteList by mutableStateOf(listOf<NoteItem>()) // передаем список заметок NoteItem

    var originNoteList = listOf<NoteItem>() // список всех заметок

    private var noteItem: NoteItem? = null // переменная для сохранения

    private val _uiEvent = Channel<UiEvent>() // переменная-передатчик; в <> указываем,
    // что будем передавать UiEvent

    val uiEvent = _uiEvent.receiveAsFlow() // переменная-приемник (берем созданный канал и
    // превращем его в приемник .receiveAsFlow() )

    var titleColor =
        mutableStateOf("#FF000000") //переменная для цвета текста заголовка (значение по умолчанию)

    var searchText by mutableStateOf("") // переменная для текста, вводимого в Поиск (по умолчанию Пусто)
        private set
    val uid = auth.currentUser?.uid ?: "" // для получения uid пользователя

    // переменные из interface DialogController для управляения Диалогом
    override var dialogTitle =
        mutableStateOf("Delete this note?") // для изменения заголовка Диалога
        private set // чтобы записать значение в переменную только из текущего класса
    override var editableText = mutableStateOf("") // текст, вводимый внутри Диалога
        private set
    override var openDialog = mutableStateOf(false) // открыть(закрыть) окно Диалога
        private set
    override var showEditableText =
        mutableStateOf(false) // показать (спрятать) поле для ввода текста
        private set

    init {
        viewModelScope.launch { // запускаем Корутину
            dataStoreManager.getStringPreference(
                DataStoreManager.TITLE_COLOR, // выбранный цвет заголовка в Настройках
                "#FF000000" // значение цвета по умолчанию
            ).collect { color -> // запускаем .collect т.к. это Flow
                titleColor.value = color // записываем цвет в переменную titleColor
            }
        }
    }

    // Ф-ия событий для бизнес логики (куда передаем события с экрана)
    fun onEvent(event: NoteListEvent) { // передаем события NoteListEvent

        when (event) { // обрабатываем события

            is NoteListEvent.OnTextSearchChange -> {
                searchText = event.text // в переменную для текста Поиска записываем текст из event
                noteList = originNoteList.filter { note ->
                    note.title.lowercase()
                        .startsWith(searchText.lowercase()) // переводим заголовок в lowercase и
                    // сравниваем с searchText в lowercase
                }
            }

            is NoteListEvent.OnShowDeleteDialog -> {
                openDialog.value = true // вызываем диалог
                noteItem =
                    event.item // сохраняем item в переменную noteItem (глобальная переменная,
                // будет доступна в ф-ии OnConfirm )
            }

            is NoteListEvent.OnItemClick -> { // нажатие на Заметку
                // Это событие экрана
                sendUiEvent(UiEvent.Navigate(event.route + "/${auth.uid}")) // отправляем Ui событие и указываем
                // в Navigate куда мы хотим перейти (на какой экран)
            }

            is NoteListEvent.UnDoneDeleteItem -> { // если пользователь Отменяет удаление
                if (noteItem != null) {
                    fireStore.collection("user_data")
                        .document("notes")
                        .collection(uid)
                        .document(noteItem?.key ?: "")
                        .set(noteItem!!)
                        .addOnSuccessListener {
                            onEvent(NoteListEvent.OnGetNoteList) // обновляем список
                        }
                }
            }

            is NoteListEvent.OnGetNoteList -> {
                fireStore.collection("user_data")
                    .document("notes")
                    .collection(uid)
                    .get()
                    .addOnSuccessListener {
                        originNoteList = it.toObjects(NoteItem::class.java) // получаем список заметок
                        noteList = originNoteList // записываем в переменную noteList
                    }
            }
        }
    }

    // Ф-ия для получения событий диалога
    override fun onDialogEvent(event: DilogEvent) {
        when (event) {
            is DilogEvent.OnCancel -> {
                openDialog.value = false // закрытие диалога
            }

            is DilogEvent.OnConfirm -> {
                fireStore.collection("user_data")
                    .document("notes")
                    .collection(uid)
                    .document(noteItem?.key ?: "")
                    .delete().addOnSuccessListener {
                        onEvent(NoteListEvent.OnGetNoteList) // обновляем список после удаления
                    }
                sendUiEvent(UiEvent.ShowSnackBar("Undone delete item?")) // отображаем SnackBar с сообщением
                openDialog.value = false // закрываем диалог
            }

            else -> {}
        }
    }

    private fun sendUiEvent(event: UiEvent) { // ф-ия для отправки события

        viewModelScope.launch { // используем Корутины для отправки события

            _uiEvent.send(event) // на переменной-передатчике вызываем ф-ию send и передаем Событие
        }
    }
}