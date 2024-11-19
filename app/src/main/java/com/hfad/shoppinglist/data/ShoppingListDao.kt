package com.hfad.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // для записи эл-та в БД
    suspend fun insertItem(item: ShoppingListItem) // suspend (используем корутины)
//    (onConflict = OnConflictStrategy.REPLACE) используется для
//    обновления (перезаписи) эл-та с помощью одной ф-ии @Insert
//    при возникновении конфликта (одинаковый id) наш элемент будет перезаписан

//    Второй способ обновлениея (перезаписи)
//    @Update // обновление (перезапись) эл-та в БД
//    suspend fun updateItem(item: ShoppingListItem)

    @Delete // удаление эл-та из БД (удаление Списка)
    suspend fun deleteItem(item: ShoppingListItem)

    @Query("SELECT * FROM shop_list_name") // получение всех эл-тов из таблицы (SQL запрос)
    fun getAllItems(): Flow<List<ShoppingListItem>> // suspend ф-ию не делаем,
//    т.к класс Flow из пакета корутин и отвечает за это
//    класс Flow будет следить за списком эл-тов и выдавать список с обновлениями

    @Query("DELETE FROM add_item WHERE listId = :listId") // удаляем элементы из
    // табл. add_item, где идентификатор listId равен переданному идентификатору
    suspend fun deleteAddItems(listId: Int) // для удаления товаров по Идентификатору

    @Transaction // обобщение ф-ий в одну
    // ф-ию, кот. удаляет и сам список и эл-ты, принадлежащие данному списку
    suspend fun deleteShoppingList(item: ShoppingListItem) {
        deleteAddItems(item.id!!) // вызываем deleteAddItems (удаления товаров - эл-тов Списка)
        deleteItem(item) // удаление Списка
    }
}