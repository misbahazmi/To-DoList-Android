package com.misbah.todo.notifications
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.ui.dialogs.DialogRepository
import com.misbah.todo.ui.utils.Constants
import com.nytimes.utils.AppLog
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
open class TaskDataRepository @Inject constructor(private val taskDao : TaskDao){
    var remainingTasks: LiveData<List<Task>>? = null
    fun deleteAllCompletedTasks(){
        taskDao.deleteCompletedTasks()
    }

    fun getRemainingTaskList() :  LiveData<List<Task>> {
        getTasksRemainingTask()
        return  remainingTasks!!
    }

    fun getTasksRemainingTask() = CoroutineScope(Dispatchers.IO).launch {
        val currentTime = System.currentTimeMillis()
        val futureTime = System.currentTimeMillis() + Constants.TASK_REMINDER_TIME_INTERVAL
        remainingTasks  =  taskDao.getTasksRemainingTask(true,currentTime,futureTime).asLiveData()
        AppLog.debugD("SIZE:::::: ${remainingTasks?.value?.size}")
    }

}