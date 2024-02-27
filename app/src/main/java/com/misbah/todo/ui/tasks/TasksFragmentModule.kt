package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.SavedStateHandle
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
class TasksFragmentModule {

    @Provides
    fun provideViewModel(taskDao : TaskDao, preferencesManager: PreferencesManager, state : SavedStateHandle, context: Context) : TasksViewModel {
        return TasksViewModel(taskDao, preferencesManager, state, context)
    }

    @Provides
    fun provideViewModelProvider(viewModel: TasksViewModel) : ViewModelProvider.Factory{
        return ViewModelFactory(viewModel)
    }

}