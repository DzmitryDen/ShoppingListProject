package com.hfad.shoppinglist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "add_item")
data class AddItem( // товар

    @PrimaryKey
    val id: Int? = null,
    val name: String, // название товара
    val isCheck: Boolean, // состояние чек-бокса
    val listId:Int // id списка
)
