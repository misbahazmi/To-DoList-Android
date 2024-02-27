package com.misbah.todo.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.misbah.todo.R
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.core.data.storage.TaskDatabase
import com.misbah.todo.ui.dialogs.DialogRepository
import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.utils.Constants
import com.nytimes.utils.AppLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class NotificationReminderReceiver @Inject constructor(): BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = context.resources.getText(R.string.app_name)
        val description = context.resources.getText(R.string.nav_header_subtitle)

        val mBuilder = NotificationCompat.Builder(context,Constants.CHANNEL_ID)

        mBuilder.setSmallIcon(R.drawable.ic_task_menu)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val resultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        //Add Tasks Intent
        val addTaskIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        addTaskIntent.extras?.putBoolean("add-tasks", true)

        //Add Tasks Pending Intent
        val addTaskPendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 110, addTaskIntent, PendingIntent.FLAG_IMMUTABLE)

        mBuilder.addAction(R.drawable.ic_add_task_menu, context.getString(R.string.menu_new_taks), addTaskPendingIntent)

        val resultPendingIntent = PendingIntent.getActivity(
            context,
            111,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(2222, mBuilder.build())

    }
}