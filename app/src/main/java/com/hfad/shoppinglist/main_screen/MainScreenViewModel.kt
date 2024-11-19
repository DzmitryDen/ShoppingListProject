package com.hfad.shoppinglist.main_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.shoppinglist.data.ShoppingListItem
import com.hfad.shoppinglist.data.ShoppingListRepository
import com.hfad.shoppinglist.dialog.DialogController
import com.hfad.shoppinglist.dialog.DilogEvent
import com.hfad.shoppinglist.utils.Routes
import com.hfad.shoppinglist.utils.TimeUtils
import com.hfad.shoppinglist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: ShoppingListRepository
) : ViewModel(), DialogController {

    private val _uiEvent = Channel<UiEvent>() // переменная-передатчик; в <> указываем,

    // что будем передавать UiEvent
    val uiEvent = _uiEvent.receiveAsFlow() // переменная-приемник (берем созданный канал и
    // превращем его в приемник .receiveAsFlow()

    // переменные из interface DialogController для управляения Диалогом
    override var dialogTitle = mutableStateOf("List name:") // заголовок Диалога
        private set // чтобы записать значение в переменную только из текущего класса
    override var editableText = mutableStateOf("") // текст, вводимый внутри Диалога (всегда пусто)
        private set
    override var openDialog = mutableStateOf(false) // открыть(закрыть) окно Диалога
        private set
    override var showEditableText =
        mutableStateOf(true) // показывать поле для ввода текста (всегда true)
        private set

    var showFloatingButton =
        mutableStateOf(true) // отобразить(скрыть) кнопкку <Добавить> FloatingButton
        private set // чтобы записать значение в переменную только из текущего класса

    fun onEvent(event: MainScreenEvent) { // создаем ф-ию onEvent, в кот. передаем события

        when (event) { // обрабатываем события
            is MainScreenEvent.OnItemSave -> {
                if (editableText.value.isEmpty()) return // если текст (название) не введен - диалог закрывается
                viewModelScope.launch {// запускается Корутина
                    repository.insertItem(
                        ShoppingListItem(
                            null, // всегда null, т.к. всегда создается новый эл-т
                            editableText.value, // значение из переменной editableText
                            TimeUtils.getCurrentTime(System.currentTimeMillis()), // вызываем ф-ию получения текущего времени
                            0, // всегда 0, т.к. это новый элемент
                            0 // отмеченных эл-тов всегда 0, т.к. это новый эл-т
                        )
                    )
                }
            }

            is MainScreenEvent.OnNewItemClick -> { // если событие - Добавление нового эл-та
                if (event.route == Routes.SHOPPING_LIST) {// если событие вызвано на экране SHOPPING_LIST
                    openDialog.value =
                        true // вызываем диалог (для создания нового списка с покупками)
                } else { // событие вызвано на экране Список заметок
                    sendUiEvent(UiEvent.NavigateMain(Routes.NEW_NOTE + "/////${event.uid}")) // открываем экран добавления
                    // новой заметки (т.к. находимся на экране добавления заметок - Список заметок) и через
                    // слэш передаем значение по умолчанию для Идентификатора -1, что означет создание
                    // нового эл-та, как прописано в NewNoteViewModel
                }
            }

            is MainScreenEvent.Navigate -> {
                sendUiEvent(UiEvent.Navigate(event.route)) // запускаем ф-ию отправки события-Navigate
                // и передаем route
                showFloatingButton.value = if ( // условие отображения (скрытия кнопки)
                    event.route == Routes.ABOUT || event.route == Routes.SETTINGS
                ) {
                    false
                } else {
                    true
                }
            }

            is MainScreenEvent.NavigateMain -> {
                sendUiEvent(UiEvent.NavigateMain(event.route)) // запускаем ф-ию отправки события-NavigateMain
                // и передаем route

            }

            is MainScreenEvent.DeleteAccount -> {
                val credential = EmailAuthProvider
                    .getCredential(event.email, event.password)
                auth.currentUser?.reauthenticate(credential)
                    ?.addOnSuccessListener {
                        auth.currentUser?.delete()?.addOnSuccessListener {
                            sendUiEvent(UiEvent.NavigateMain(Routes.LOGIN_SCREEN))
                        }
                    }
            }

            MainScreenEvent.OnSignOut -> {
                auth.signOut()
                sendUiEvent(UiEvent.NavigateMain(Routes.LOGIN_SCREEN))
            }
        }
    }

    override fun onDialogEvent(event: DilogEvent) {
        when (event) {
            is DilogEvent.OnCancel -> {
                openDialog.value = false // закрытие диалога
                editableText.value = "" // очищаем строку для ввода
            }

            is DilogEvent.OnConfirm -> {
                onEvent(MainScreenEvent.OnItemSave) // запускаем ф-ию onEvent,
                // в кот. передаем событие MainScreenEvent.OnItemSave (запустится сохранение)
                editableText.value = "" // очищаем строку для ввода
                openDialog.value = false // закрываем диалог
            }

            is DilogEvent.OnTextChange -> {
                editableText.value = event.text // передаем текст в переменную чз событие
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) { // ф-ия для отправки события

        viewModelScope.launch { // используем Корутины для отправки события

            _uiEvent.send(event) // на переменной-передатчике вызываем ф-ию send и передаем Событие
        }
    }
}