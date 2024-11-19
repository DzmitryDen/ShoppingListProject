package com.hfad.shoppinglist.new_note_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.shoppinglist.data.NoteItem
import com.hfad.shoppinglist.datastore.DataStoreManager
import com.hfad.shoppinglist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // для того чтобы исползовать класс с пом. HILT
class NewNoteViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    dataStoreManager: DataStoreManager     // передаем DataStoreManager (для получения цвета из Настроек)
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>() // переменная-передатчик; в <> указываем,
    val uiEvent = _uiEvent.receiveAsFlow() // переменная-приемник (берем созданный канал и

    // превращем его в приемник .receiveAsFlow()
    private var noteItem: NoteItem? = null // заметка, null по умолчанию
    var titleColor =
        mutableStateOf("#FF000000") //переменная для цвета текста заголовка (значение по умолчанию)

    var title by mutableStateOf("") // для ввода заголовка
        private set // редактирование (запись) только из данного класса

    var description by mutableStateOf("") // для ввода содержания заметки
        private set // редактирование (запись) только из данного класса

    init { // ф-ия запускается при инициализации класса
        viewModelScope.launch {
            dataStoreManager.getStringPreference(
                DataStoreManager.TITLE_COLOR, // выбранный цвет заголовка в Настройках
                "#FF000000" // значение цвета по умолчанию
            ).collect { color -> // запускаем .collect т.к. это Flow
                titleColor.value = color // записываем цвет в переменную titleColor
            }
        }
    }

    fun onEvent(event: NewNoteEvent) { // ф-ия для событий
        when (event) { // проверяем и реализуем обработчики для событий
            is NewNoteEvent.OnSave -> { // сохранение
                viewModelScope.launch {// запускаем Корутину

                    if (title.isEmpty()) { // если title пустой
                        sendUiEvent(
                            UiEvent.ShowSnackBar( // отображаем SnackBar с сообщением
                                "Title can not be empty!"
                            )
                        )
                        return@launch // Корутина завершится недойдя до insertItem
                    }

                    val key = noteItem?.key ?: fireStore.collection("user_data")
                        .document("notes").collection(event.uid)
                        .document()
                        .id
                    fireStore.collection("user_data")
                        .document("notes")
                        .collection(event.uid)
                        .document(key)
                        .set(
                            NoteItem(
                                key,
                                title,
                                description,
                                noteItem?.timeInMillis ?: System.currentTimeMillis() // если noteItem?.time == null,
                                // то это создание новой заметки - вызываем ф-ию для получения текущего времени
                                // в данном случае время создания заметки при ее редактировании изменяться не будет
                            )
                        ).addOnSuccessListener {
                            sendUiEvent(UiEvent.PopBackStack) // возвращение на экран Список заметок
                        }
                }
            }

            is NewNoteEvent.OnTitleChange -> { // изменение текста в title
                title = event.title //в title передаем title из события
            }

            is NewNoteEvent.OnDescriptionChange -> { // изменение текста в description
                description = event.description //в description передаем description из события
            }

            is NewNoteEvent.OnGetEditNote -> { // изменение текста в description
                noteItem = event.noteItem
                title = event.noteItem?.title ?: ""
                description =
                    event.noteItem?.description
                        ?: "" //в description передаем description из события
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) { // ф-ия для отправки события

        viewModelScope.launch { // используем Корутины для отправки события

            _uiEvent.send(event) // на переменной-передатчике вызываем ф-ию send и передаем Событие
        }
    }
}