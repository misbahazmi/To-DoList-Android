package com.misbah.todo.notifications

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.misbah.todo.R
import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.utils.Constants
import com.misbah.todo.ui.utils.vectorToBitmap
import com.nytimes.utils.AppLog

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class NotificationWorker (
   appContext: Context,
    workerParams: WorkerParameters
): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val id = 111
        val title = inputData.getString("NAME") ?: applicationContext.getString(R.string.app_name)
        val message = inputData.getString("DATA") ?: applicationContext.getString(R.string.nav_header_subtitle)
        AppLog.debugD("=====TASK NAME: $title")
        AppLog.debugD("=====TASK DETAILS: $message")
        val output = workDataOf("TASK_NAME" to title)
        sendNotification(id,title,message ?:"")
        return Result.success(output)
    }

    private fun sendNotification(id: Int,title : String, message  :String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = if (SDK_INT >= S) {
            getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        val bitmap = applicationContext.vectorToBitmap(R.mipmap.ic_launcher)
        val notification = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_task_menu)
            .setContentTitle(title).setContentText(message)
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        val name = applicationContext.getString(R.string.channel_name)
        val descriptionText = applicationContext.getString(R.string.channel_description)
        notification.setChannelId(Constants.CHANNEL_ID)
        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION).build()

        val channel =
            NotificationChannel(Constants.CHANNEL_ID, name, IMPORTANCE_HIGH).apply {
                description = descriptionText
            }
        channel.enableLights(true)
        channel.lightColor = RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
    }
}