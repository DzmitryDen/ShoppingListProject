package com.hfad.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AddItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: AddItem)

    @Delete
    suspend fun deleteItem(item: AddItem) // suspend (используем корутины)

    // Для получения всех элемнтов, кот принадлежат данному Списку
    @Query("SELECT * FROM add_item WHERE listId = :listId") // где listId эл-та в таблице = переданному в ф-ию :listId
    fun getAllItemsById(listId: Int): Flow<List<AddItem>> // передаем идентификатор Списка, к кот. принадлежит эл-т

    // Для получения выбранного Списка как эл-та
    @Query("SELECT * FROM shop_list_name WHERE id = :listId") // где primary Списка, в кот. переходим = переданному в ф-ию :listId
    suspend fun getListItemById(listId: Int): ShoppingListItem // выдаст 1 эл-т (suspend т.к. не используем класс с корутинами Flow)

    // Для перезаписи Списка (обновленного состояния - счетчика)
    @Update // для обновления в БД
    suspend fun insertItem(item: ShoppingListItem)
}