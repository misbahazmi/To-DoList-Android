package com.misbah.todo.ui.settings

import androidx.lifecycle.SavedStateHandle
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
class SettingsFragmentModule {

    @Provides
    fun provideViewModel(preferencesManager: PreferencesManager) : SettingsViewModel {
        return SettingsViewModel(preferencesManager)
    }

    @Provides
    fun provideViewModelProvider(viewModel: SettingsViewModel) : ViewModelProvider.Factory{
        return ViewModelFactory(viewModel)
    }

}