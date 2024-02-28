package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.core.di.factory.ViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
class AddEditFragmentModule {

    @Provides
    fun provideViewModel(taskDao : TaskDao, state : Context) : AddEditTaskViewModel {
        return AddEditTaskViewModel(taskDao, state)
    }

    @Provides
    fun provideViewModelProvider(viewModel: AddEditTaskViewModel) : ViewModelProvider.Factory{
        return ViewModelFactory(viewModel)
    }

}