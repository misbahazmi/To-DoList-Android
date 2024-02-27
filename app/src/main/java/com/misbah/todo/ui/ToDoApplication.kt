package com.misbah.todo.ui

import com.misbah.todo.core.di.component.DaggerMainComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class ToDoApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return  DaggerMainComponent.builder().application(this).build()
    }


}