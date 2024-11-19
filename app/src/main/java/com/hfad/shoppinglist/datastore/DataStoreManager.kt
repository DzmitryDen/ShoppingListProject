package com.hfad.shoppinglist.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


const val DATA_STORE_NAME = "preference_storage_name" // задаем Название хранилища
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME) // инициализируем
//dataStore через Context с помощью by и пеоедаем Название DATA_STORE_NAME
// Теперь в Контексте есть переменная dataStore, у кот. есть доступ к Preference, чтобы можно было
// сохранять значение ввиде ключ-значение

class DataStoreManager(val context: Context) { // передаем Контекст, т.к. в контексте будет спец. объект dataStore,
// с помощью кот. будем получать доступ к хранилищу

    //ф-ия для сохранения значения (suspend т.к. будет запускаться в Корутине)
    suspend fun saveStingPreference( // передаем value: String значение для сохранения и key: String ключ
        value: String,
        key: String
    ) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey(key)] = // т.к. ключ String, то передаем его в preference чз спец. ф-ию stringPreferencesKey
                value
        }
    }

    //ф-ия для получения значения (будет выдавать Flow, что уже явл. Корутиной)
    fun getStringPreference(key: String, defValue: String) = // передаем key: String ключ и значение
        // по умолчанию если под заданным ключом будет null
        context.dataStore.data.map { pref ->
            pref[stringPreferencesKey(key)] ?: defValue // т.к. ключ String, то передаем его в preference
            // чз спец. ф-ию stringPreferencesKey; если под ключом null то выдаст знач. по умолчанию defValue
        }

    companion object {
        const val TITLE_COLOR = "title_color" // ключ для записи(считывания) значения
    }
}