package com.hfad.shoppinglist.utils

object ProgressHelper {

    fun getProgress(// ф-ия для вычисления прогресса,
        allItemsCount: Int,
        selectedItemsCount: Int
    ): Float { // в аргументы передаем общее кол-во эл-тов (товаров) и отмеченное кол-во эл-тов (товаров)
        // возвращаем значение Float
        return if (selectedItemsCount == 0) 0.0f // если нет отмеченных элементов возвращаем 0
        else selectedItemsCount.toFloat() / allItemsCount
    }
}