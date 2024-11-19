package com.hfad.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_list_name") // в скобках название таблицы на основе класса
data class ShoppingListItem( // список

    @PrimaryKey // основной идентификатор для БД
    val id: Int? = null, // id списка
    val name: String, // наименование списка
    val time: String,
    val allItemsCount: Int, // счетчик товаров
    val allSelectedItemsCount: Int // счетчик отмеченных товаров
)
