package com.hfad.shoppinglist.settings_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.shoppinglist.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val colorItemListState =
        mutableStateOf<List<ColorItem>>(emptyList()) // список ColorItem, по умолчанию пустой

    init {
        viewModelScope.launch {// запускаем Корутину
            dataStoreManager.getStringPreference(
                DataStoreManager.TITLE_COLOR, // ключ
                "#487242" // значение по умолчанию (первое из ColorUtils)
            ).collect { selectedColor -> // из Flow достаем значение с помощью collect

                val tempColorItemList =
                    ArrayList<ColorItem>() // создаем временный список с эл-тами ColorItem,

                ColorUtils.colorList.forEach { color -> // список цветов из ColorUtils перебираем с помощью forEach
                    tempColorItemList.add( // для каждого цвета добавляем объект ColorItem в список
                        ColorItem( // создаем объект ColorItem
                            color,
                            selectedColor == color // при совпадении цветов выдаст true и отобразится галочка
                        )
                    )
                }
                colorItemListState.value =
                    tempColorItemList // после прохождения цикла передаем список
                // tempColorItemList в colorItemListState для дальнейшего использования на экране
            }
        }
    }

    // обработчик событий
    fun onEvent(event: SettingsEvent) { // передаем событие
        when (event) { // проверяем событие (в данном случае у нас одно событие)
            is SettingsEvent.OnItemSelected -> {
                viewModelScope.launch { // запускаем Корутину
                    dataStoreManager.saveStingPreference( //сохраняем значение
                        event.color, // цвет из события
                        DataStoreManager.TITLE_COLOR // ключ
                    )
                }
            }
        }
    }
}