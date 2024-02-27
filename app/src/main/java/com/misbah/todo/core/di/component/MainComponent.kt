package com.misbah.todo.core.di.component

import com.misbah.todo.core.di.builder.FragmentBuilder
import com.misbah.todo.core.di.module.NetworkModule
import com.misbah.todo.ui.ToDoApplication
import com.misbah.todo.core.di.builder.ActivityBuilder
import com.misbah.todo.core.di.builder.BroadcastReceiverModule
import com.misbah.todo.core.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        ActivityBuilder::class,
        FragmentBuilder::class,
        BroadcastReceiverModule::class
    ]
)

interface MainComponent : AndroidInjector<ToDoApplication> {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: ToDoApplication): Builder
        fun build(): MainComponent
    }
}