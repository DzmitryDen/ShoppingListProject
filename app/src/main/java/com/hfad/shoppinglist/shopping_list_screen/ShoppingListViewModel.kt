package com.hfad.shoppinglist.shopping_list_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.shoppinglist.data.ShoppingListItem
import com.hfad.shoppinglist.data.ShoppingListRepository
import com.hfad.shoppinglist.dialog.DilogEvent
import com.hfad.shoppinglist.dialog.DialogController
import com.hfad.shoppinglist.utils.TimeUtils
import com.hfad.shoppinglist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // указание для Hilt, что данный класс является ViewModel
class ShoppingListViewModel @Inject constructor( // @Inject вставляет конструктор
    private val repository: ShoppingListRepository // передаем ShoppingListRepository
) : ViewModel(),
    DialogController { // наследуемся от ViewModel, подключаем interface DialogController

    val list =
        repository.getAllItems() // список эл-тов (мы получаем Flow, где уже имеются Корутины)

    private val _uiEvent = Channel<UiEvent>() // переменная-передатчик; в <> указываем,
    // что будем передавать UiEvent

    val uiEvent = _uiEvent.receiveAsFlow() // переменная-приемник (берем созданный канал и
    // превращем его в приемник .receiveAsFlow() )

    private var listItem: ShoppingListItem? = null // переменная для сохранения

    // переменные из interface DialogController для управляения Диалогом
    override var dialogTitle = mutableStateOf("") // для изменения заголовка Диалога
        private set // чтобы записать значение в переменную только из текущего класса
    override var editableText = mutableStateOf("") // текст, вводимый внутри Диалога
        private set
    override var openDialog = mutableStateOf(false) // открыть(закрыть) окно Диалога
        private set
    override var showEditableText =
        mutableStateOf(false) // показать (спрятать) поле для ввода текста
        private set


    fun onEvent(event: ShoppingListEvent) { // создаем ф-ию onEvent, в кот. передаем события

        when (event) { // обрабатываем события
            is ShoppingListEvent.OnItemSave -> {
                if (editableText.value.isEmpty()) return // проверка на пусто
                viewModelScope.launch {// запускается Корутина
                    repository.insertItem(
                        ShoppingListItem(
                            listItem?.id,
                            editableText.value, // значение из переменной editableText
                            listItem?.time
                                ?: TimeUtils.getCurrentTime(System.currentTimeMillis()), // берем время из listItem,
                            // если оно есть, и если оно null, то вызываем ф-ию getCurrentTime()
                            // для получения текущего времени
                            listItem?.allItemsCount ?: 0, // если null, то будет присвоен 0
                            listItem?.allSelectedItemsCount ?: 0
                        )
                    )
                }
            }

            is ShoppingListEvent.OnItemClick -> {
                sendUiEvent(UiEvent.Navigate(event.route)) // отправляем Ui событие и указываем
                // в Navigate куда мы хотим перейти (на какой экран)
            }

            is ShoppingListEvent.OnShowEditDialog -> {
                listItem = event.item // запись эл-та
                openDialog.value = true // вызываем диалог (открытие)
                editableText.value = listItem?.name
                    ?: "" // передаем текст из названия или ничего для создания нового эл-та
                dialogTitle.value = "List name:" // название диалога
                showEditableText.value = true // показываем поле для ввода текста
            }

            is ShoppingListEvent.OnShowDeleteDialog -> {
                listItem = event.item // запись эл-та, кот. хотим удалить
                openDialog.value = true // вызываем диалог (открытие)
                dialogTitle.value = "Delete this item?" // название диалога
                showEditableText.value = false // Не показываем поле для ввода текста
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
                if (showEditableText.value) { // проверяем наличие поля для ввода текста (поле показано)
                    onEvent(ShoppingListEvent.OnItemSave) // запускаем ф-ию onEvent,
                    // в кот. передаем событие ShoppingListEvent.OnItemSave (запстится сохранение)
                } else { // полея для ввода текста не показано
                    viewModelScope.launch { // делаем удаление в отдельном потоке в Корутинах
                        listItem?.let { repository.deleteItem(it) } // проверка на null:
                        // если listItem не null, то запускаем repository.deleteItem(it), где it это listItem

                    }
                }
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