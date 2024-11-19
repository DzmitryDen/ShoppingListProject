package com.hfad.shoppinglist.settings_screen

// события
sealed class SettingsEvent {
    data class OnItemSelected(val color:String): SettingsEvent() // выбор цвета
}