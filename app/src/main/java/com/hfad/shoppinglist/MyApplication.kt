package com.hfad.shoppinglist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //используется для инициализации Hilt в приложении
// после добавления аннотации, Hilt берет на себя управление зависимостями
class MyApplication : Application() {
}