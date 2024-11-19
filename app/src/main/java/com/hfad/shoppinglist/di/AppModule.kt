package com.hfad.shoppinglist.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hfad.shoppinglist.data.AddItemRepoImpl
import com.hfad.shoppinglist.data.AddItemRepository
import com.hfad.shoppinglist.data.MainDb
import com.hfad.shoppinglist.data.ShoppingListRepoImpl
import com.hfad.shoppinglist.data.ShoppingListRepository
import com.hfad.shoppinglist.datastore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // аннотация указывает, что это модуль для Dugger Hilt
@InstallIn(SingletonComponent::class) // куда будет установлен модуль (пока живет приложение - класс Application, наш модуль там сохранен)
object AppModule {
    // Прописываем ф-ию для Dugger Hilt для инициализации Firebase Authentication
    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth {
        return Firebase.auth
    }

    // Прописываем ф-ию для Dugger Hilt для инициализации FirebaseFirestore
    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }


    // Прописываем ф-ию для Dugger Hilt для инициализации БД
    @Provides
    @Singleton // для однократного создания БД
    fun provideMainDb(app: Application): MainDb { // Dugger Hilt сам передаст в ф-ию Application
        // и вернется инициализированная БД  MainDb
        return Room.databaseBuilder( // инициализируем и возвращаем нашу БД
            app, // передаем Application
            MainDb::class.java, // передаем наш класс MainDb
            name = "shop_list_db" // передаем название
        ).build() // билдим
    }

    // Прописываем ф-ии для для инициализации Репозиториeв ShoppingListRepository,
    // AddItemRepository и NoteRepository
    @Provides
    @Singleton
    fun provideShopRepo(db: MainDb): ShoppingListRepository { // Dugger Hilt сам пердаст в ф-ию MainDb
        return ShoppingListRepoImpl(db.shoppingListDao) // в конструктор класса передаем ShoppingListDao
    }

    @Provides
    @Singleton
    fun provideAddItemRepo(db: MainDb): AddItemRepository {
        return AddItemRepoImpl(db.addItemDao)
    }

    // Т.к инициализация БД и репозиториев находятся в одном модуле,
    // то Dugger Hilt знает, что необходимо создать БД и передать ее
    // в ф-ии инициализации Репозиториев

    @Provides
    @Singleton
    // создаем для DataStoreManager
    fun provideDataStoreManager(app: Application): DataStoreManager {
        return DataStoreManager(app) // прописываем как инициализировать - объект DataStoreManager, передаем
    // app - т.е. Контекст
    }
}