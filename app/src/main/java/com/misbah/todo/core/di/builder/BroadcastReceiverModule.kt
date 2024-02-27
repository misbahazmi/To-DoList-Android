package com.misbah.todo.core.di.builder

import com.misbah.todo.notifications.NotificationReminderModule
import com.misbah.todo.notifications.NotificationReminderReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector( modules = [NotificationReminderModule::class])
    abstract fun contributesNotificationReminderReceiver() : NotificationReminderReceiver

}