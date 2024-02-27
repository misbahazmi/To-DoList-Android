package com.misbah.todo.notifications

import androidx.lifecycle.ViewModelProvider
import com.misbah.todo.core.data.storage.PreferencesManager
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.core.di.factory.ViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
class NotificationReminderModule {
    @Provides
    fun provideTaskDataRepository(taskDao : TaskDao) : TaskDataRepository {
        return TaskDataRepository(taskDao)
    }
}