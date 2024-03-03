package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.notifications.NotificationWorker
import com.misbah.todo.ui.main.ADD_TASK_RESULT_OK
import com.misbah.todo.ui.main.EDIT_TASK_RESULT_OK
import com.misbah.todo.ui.utils.Constants
import com.nytimes.utils.AppEnums
import com.nytimes.utils.AppLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class AddEditTaskViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val context : Context
) : ViewModel() {

    var task = MutableLiveData<ToDo>()
    var nextTaskId = MutableLiveData<Int>(0)
    val taskRepository = TaskRepository(context)
    var selectedDateTime = task.value?.due ?: System.currentTimeMillis()
    var taskTitle = task.value?.title ?: ""
    var taskDescription = task.value?.name ?: ""
    var taskImportance = task.value?.priorityValue ?: 0
    var taskImportanceString = task.value?.displayPriority ?: AppEnums.TasksPriority.Normal.name
    var tasksCategory = task.value?.categoryValue ?: 0
    var tasksCategoryString = task.value?.category ?: AppEnums.TasksCategory.General.name

    var dueDate = selectedDateTime


    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    fun onSaveClick() {
        if (taskTitle.isBlank()) {
            showInvalidInputMessage("Title cannot be empty")
            return
        }
        if (taskDescription.isBlank()) {
            showInvalidInputMessage("Description cannot be empty")
            return
        }
        if (task.value != null) {
            val updatedTask =  task.value!!.copy(name = taskDescription, title = taskTitle , important = taskImportanceString, priority = taskImportance , category = tasksCategoryString,categoryId = tasksCategory, due = dueDate)
            updateTask(updatedTask)
            try {
                WorkManager.getInstance(context).cancelAllWorkByTag(task.value?.due.toString())
                //Update Schedule Tasks
                createTaskReminder(taskTitle, taskDescription, dueDate.toString(), dueDate)
            } catch (e: Exception){
                e.localizedMessage?.let { AppLog.debugD(it) }
            }
        } else {
            val id = nextTaskId.value
            val newTask = ToDo(name = taskDescription, title = taskTitle, important = taskImportanceString, priority = taskImportance , category = tasksCategoryString, categoryId = tasksCategory, due = dueDate, id = id!!+1, userId = 1, createdLong = System.currentTimeMillis(), completed =  false, created = Date().toString())
            createTask(newTask)
            //Schedule Tasks
            createTaskReminder(taskTitle, taskDescription, dueDate.toString(), dueDate)
        }
    }

    private fun createTaskReminder(title: String, details: String, tag : String, dueDate : Long){
        val timeStamp = System.currentTimeMillis()
        if ((dueDate - timeStamp) > Constants.TASK_REMINDER_TIME_INTERVAL + 5000){
            AppLog.debugD("===Schedule Reminders===")
            val data = Data.Builder()
                .putString("NAME", title)
                .putString("DATA", details)
                .build()
            val reminderRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .setInitialDelay((dueDate - Constants.TASK_REMINDER_TIME_INTERVAL) - timeStamp, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build()
            WorkManager.getInstance(context).enqueue(reminderRequest)
        }
    }

    fun showDatePicker(){
        showDateTimePicker()
    }

    fun onDateTimeResult(result: Long) {
        dateTimeWithResult(result)
    }

    private fun createTask(task: ToDo) =  CoroutineScope(Dispatchers.IO).launch {
        taskRepository.saveTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    fun getSizeOfTaskList()= CoroutineScope(Dispatchers.IO).launch {
        nextTaskId.value = taskRepository.getTaskListSize()
    }


    private fun updateTask(task: ToDo) =  CoroutineScope(Dispatchers.IO).launch {
        taskRepository.updateTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }

    private fun dateTimeWithResult(text: Long) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.DateTimeWithResult(text))
    }

    private fun showDateTimePicker() = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowDateTimePicker)
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
        data object ShowDateTimePicker : AddEditTaskEvent()
        data class DateTimeWithResult(val result: Long) : AddEditTaskEvent()
    }
}