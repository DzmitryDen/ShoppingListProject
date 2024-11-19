package com.hfad.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database( // в анотацию передаем все entity ввиде массив и указываем версию
    entities = [ShoppingListItem::class, AddItem::class],
//    autoMigrations = [AutoMigration(from = 1, to = 2)], // указываем автоматическую миграцию
    version = 1, // при изменении версии необходимо изменить значение
    exportSchema = true // для экспорта схемы прежней версии
)
abstract class MainDb : RoomDatabase() { // класс для создания БД

    abstract val shoppingListDao: ShoppingListDao // передаем интерфейсы
    abstract val addItemDao: AddItemDao
}