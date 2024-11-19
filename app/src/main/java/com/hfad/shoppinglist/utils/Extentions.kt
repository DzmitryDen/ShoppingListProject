package com.hfad.shoppinglist.utils

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUtils {
    fun getCurrentTime(timeInMillis: Long): String { // ф-ия расширения для класса ViewModel

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) // форматер для
        // отображения даты и времени в заданном формате

        val cv = Calendar.getInstance() // инстанция календаря для получения времени
        cv.timeInMillis = timeInMillis // устанавливаем время в календаре
        return formatter.format(cv.time) // возвращаем время в отформатированном виде
    }
}