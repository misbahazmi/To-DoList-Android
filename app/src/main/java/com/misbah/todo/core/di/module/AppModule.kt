package com.misbah.todo.core.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.misbah.todo.core.data.remote.APIService
import com.misbah.todo.core.data.remote.RemoteDataSource
import com.misbah.todo.core.data.remote.NetworkConnectionInterceptor
import com.misbah.todo.ui.utils.Utils
import com.google.gson.Gson
import com.misbah.todo.core.data.storage.PreferencesManager
import com.misbah.todo.core.data.storage.TaskDatabase
import com.misbah.todo.ui.ToDoApplication
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: ToDoApplication): Context = application

    @Provides
    @Singleton
    fun provideApplication(app: ToDoApplication): Application = app

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideSavedStateHandle() = SavedStateHandle()

    @Singleton
    @Provides
    fun provideUtils(context: Context) = Utils(context = context)

    @Provides
    @Singleton
    @Named("DEFAULT")
    fun provideDefaultDispatchers(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @Named("IO")
    fun provideBackgroundDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @Named("MAIN")
    fun provideMainDispatchers(): CoroutineDispatcher = Dispatchers.Main

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: TaskDatabase.Callback
    ) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    @Singleton
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideNetworkConnectionInterceptor(utils: Utils) = NetworkConnectionInterceptor(utils)

    @Provides
    @Singleton
    fun providePreferencesManager(context : Context) = PreferencesManager(context)


    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: APIService) = RemoteDataSource(apiService)

}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope