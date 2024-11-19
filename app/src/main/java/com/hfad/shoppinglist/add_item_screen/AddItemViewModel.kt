package com.hfad.shoppinglist.add_item_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.shoppinglist.data.AddItem
import com.hfad.shoppinglist.data.AddItemRepository
import com.hfad.shoppinglist.data.ShoppingListItem
import com.hfad.shoppinglist.dialog.DialogController
import com.hfad.shoppinglist.dialog.DilogEvent
import com.hfad.shoppinglist.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: AddItemRepository,
    savedStateHandle: SavedStateHandle // для получения доступа к сохраненным аргументам, кот. мы передаем
    // при открытии экрана (данный класс инициализируется в активити и во ViewModel есть к нему доступ)
) : ViewModel(), DialogController { // DialogController для отображения диалога

    private val _uiEvent = Channel<UiEvent>() // переменная-передатчик; в <> указываем,
    // что будем передавать UiEvent
    val uiEvent = _uiEvent.receiveAsFlow() // переменная-приемник (берем созданный канал и
    // превращем его в приемник .receiveAsFlow()

    var itemsList: Flow<List<AddItem>>? = null // переменная для записи списка, после его получения
    var addItem: AddItem? = null // переменная AddItem изначально null
    var listId: Int =
        -1 // глобальная переменная listId (инициализируем -1, т.к. null у нас никогда не будет)
    var shoppingListItem: ShoppingListItem? = null // переменная для хранения ShoppingListItem

    init { // ф-ия запускается при инициализации класса
        listId = savedStateHandle.get<String>("listId")
            ?.toInt()!! // получаем из SavedStateHandle по ключу "listId" и преобразуем к Int
        itemsList = repository.getAllItemsById(listId)

        viewModelScope.launch { // запускаем Корутину
            shoppingListItem = repository.getListItemById(listId) // получаем ShoppingListItem
        }
    }

    var itemText = mutableStateOf("") // для ввода названия эл-та (товара)
        private set // редактирование (запись) только из данного класса

    // переменные из interface DialogController для управляения Диалогом
    override var dialogTitle = mutableStateOf("Edit name:") // заголовок Диалога
        private set // чтобы записать значение в переменную только из текущего класса
    override var editableText = mutableStateOf("") // текст, вводимый внутри Диалога (всегда пусто)
        private set
    override var openDialog = mutableStateOf(false) // открыть(закрыть) окно Диалога
        private set
    override var showEditableText =
        mutableStateOf(true) // показывать поле для ввода текста (всегда true)
        private set

    fun onEvent(event: AddItemEvent) { // ф-ия для событий (передаем AddItemEvent)
        when (event) { // проверяем и реализуем обработчики для событий
            is AddItemEvent.OnItemSave -> { // сохранения
                viewModelScope.launch { // запускаем Корутину
                    if (listId == -1) return@launch
                    if (addItem != null) { // проверяем что addItem не пустой
                        if (addItem!!.name.isEmpty()) { // если addItem!!.name пустой (попытка сохранить
                            // пустой диалог редактирования)
                            sendUiEvent(UiEvent.ShowSnackBar("Name must not be empty!")) // показываем SnackBar
                            //с сообщением
                            return@launch // чтобы не сохранять пустой эл-т
                        }
                    } else { // если addItem пустой (т.е. мы не редактируем, а создаем новый эл-т)
                        if (itemText.value.isEmpty()) { // если itemText.value пустой (попытка сохранить
                            // пустой эл-т при создании)
                            sendUiEvent(UiEvent.ShowSnackBar("Name must not be empty!")) // показываем SnackBar
                            //с сообщением
                            return@launch // чтобы не сохранять пустой эл-т
                        }
                    }
                    repository.insertItem(
                        AddItem( // создаем экземпляр AddItem
                            addItem?.id, // id товара
                            addItem?.name ?: itemText.value, // название товара, если не null,
                            // то берем из addItem?.name, если null, то это создание нового, из itemText.value
                            addItem?.isCheck
                                ?: false, // состояние чек-бокса (если null, то создаем новый эл-т)
                            listId
                        )
                    )
                    itemText.value = "" // очищаем строку для ввода названия товара
                    addItem = null // очищаем addItem
                }

                updateShoppingListCount() // обновление после добавления
            }

            is AddItemEvent.OnShowEditDialog -> {
                addItem = event.item
                openDialog.value = true // открываем диалог
                editableText.value = addItem?.name ?: "" // в строку редактирования в Диалоге
                // передаем name из addItem или пустую строку, если null
            }

            is AddItemEvent.OnTextChange -> {
                itemText.value = event.text // берем текст из события
            }

            is AddItemEvent.OnDelete -> {
                viewModelScope.launch { // запускаем Корутину
                    repository.deleteItem(event.item) // передаем item для удаления из события
                }
                updateShoppingListCount() // обновление после удаления
            }

            is AddItemEvent.OnCheckedChange -> {
                viewModelScope.launch { // запускаем Корутину
                    repository.insertItem(event.item) // перезаписываем item (берем item, кот. приходит в событии)
                }
                updateShoppingListCount() // обновление после установки-снятии чек-бокса
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
                addItem =
                    addItem?.copy(name = editableText.value) // в addItem копируем текст из editableText.value,
                // т.е. name мы изменим на то, что напишем в диалоге
                openDialog.value = false // закрываем диалог
                editableText.value = "" // очищаем строку для ввода
                onEvent(AddItemEvent.OnItemSave) // запускаем событие AddItemEvent.OnItemSave
            }

            is DilogEvent.OnTextChange -> {
                editableText.value = event.text // передаем текст в переменную чз событие
            }
        }
    }

    private fun updateShoppingListCount() {
        viewModelScope.launch {// запускаем Корутину
            itemsList?.collect { list ->// берем itemsList (это Flow), вызываем ф-ию .collect, кот. выдает список эл-тов
                var counter = 0 // переменная счетчик
                list.forEach { item -> // перебираем список по эл-там
                    if (item.isCheck) { // проверяем отмечен эл-т в чек-боксе или нет
                        counter++ // увеличиваем счетчик на 1
                    }
                }


                shoppingListItem?.copy( // перезаписываем shoppingListItem
                    allItemsCount = list.size, // общее кол-во эл-тов в списке
                    allSelectedItemsCount = counter // количество отмеченных эл-тов
                )?.let { shItem ->
                    repository.insertItem(
                        shItem
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) { // ф-ия для отправки события

        viewModelScope.launch { // используем Корутины для отправки события

            _uiEvent.send(event) // на переменной-передатчике вызываем ф-ию send и передаем Событие
        }
    }

}