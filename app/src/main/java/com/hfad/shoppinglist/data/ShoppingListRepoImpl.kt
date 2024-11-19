package com.hfad.shoppinglist.data

import kotlinx.coroutines.flow.Flow

class ShoppingListRepoImpl(
    private val dao: ShoppingListDao
) : ShoppingListRepository {
    override suspend fun insertItem(item: ShoppingListItem) {
        dao.insertItem(item)
    }

    override suspend fun deleteItem(item: ShoppingListItem) {
        dao.deleteShoppingList(item) // здесь запустятся 2 ф-ии deleteAddItems (удаление эл-тов Списка)
    // и deleteItem (удаление Списка)
    }

    override fun getAllItems(): Flow<List<ShoppingListItem>> {
        return dao.getAllItems()
    }
}