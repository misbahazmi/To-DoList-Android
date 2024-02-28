package com.misbah.todo.ui.dialogs

import androidx.lifecycle.ViewModelProvider
import com.misbah.todo.core.data.storage.PreferencesManager
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
class DialogFragmentModule {

    @Provides
    fun provideViewModel(repository: DialogRepository) : DialogViewModel {
        return DialogViewModel(repository)
    }

    @Provides
    fun provideDialogRepository(taskDao : TaskDao) : DialogRepository {
        return DialogRepository(taskDao)
    }

    @Provides
    fun provideViewModelProvider(viewModel: DialogViewModel) : ViewModelProvider.Factory{
        return ViewModelFactory(viewModel)
    }

}