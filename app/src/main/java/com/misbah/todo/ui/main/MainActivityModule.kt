package com.misbah.todo.ui.main

import androidx.lifecycle.ViewModelProvider
import com.misbah.todo.core.di.factory.ViewModelFactory
import dagger.Module
import dagger.Provides

/**
 * @author: MOHAMMAD MISBAH
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Android Role
 * @desc MainActivityModule for dependencies injections
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java||Flutter
 */

@Module
class MainActivityModule {

    @Provides
    fun providesViewModel() : MainViewModel {
        return MainViewModel()
    }

    @Provides
    fun provideViewModelProvider(viewModel: MainViewModel) : ViewModelProvider.Factory{
        return ViewModelFactory(viewModel)
    }
}