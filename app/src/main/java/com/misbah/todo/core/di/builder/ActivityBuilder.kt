package com.misbah.todo.core.di.builder


import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.main.MainActivityModule
import com.misbah.todo.ui.main.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeSplashActivity(): SplashActivity
}