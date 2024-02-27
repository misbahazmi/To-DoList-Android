package com.misbah.todo.ui.tasks

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.data.storage.PreferencesManager
import com.misbah.todo.core.data.storage.SortOrder
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.notifications.NotificationWorker
import com.misbah.todo.ui.main.ADD_TASK_RESULT_OK
import com.misbah.todo.ui.main.EDIT_TASK_RESULT_OK
import com.misbah.todo.ui.utils.Constants
import com.nytimes.utils.AppLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class TasksViewModel
@Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle,
    private val context: Context
)  : ViewModel() {
    val searchQuery = state.getLiveData("searchQuery", "")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted, filterPreferences.category)
    }
    val tasks = tasksFlow.asLiveData()

    var remainingTasks: LiveData<List<Task>>? = null

    fun onSortOrderSelected(sortOrder: SortOrder) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onFilterCategoryClick(taskCategory: Int) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateTaskCategory(taskCategory)
    }

    fun onTaskSelected(task: Task) = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun getTasksRemainingTask() = CoroutineScope(Dispatchers.IO).launch {
        val currentTime = System.currentTimeMillis()
        val futureTime = System.currentTimeMillis() + Constants.TASK_REMINDER_TIME_INTERVAL
        remainingTasks = taskDao.getTasksRemainingTask(true,currentTime,futureTime).asLiveData()
    }

    fun onTaskCheckedChanged(task: Task, isChecked: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            taskDao.delete(task)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
            try {
                WorkManager.getInstance(context).cancelAllWorkByTag(task.due.toString())
            } catch (e: Exception){
                e.localizedMessage?.let { AppLog.debugD(it) }
            }
        }
    }
    fun onUndoDeleteClick(task: Task) = CoroutineScope(Dispatchers.IO).launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(text: String) = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick() = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    sealed class TasksEvent {
        data object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        data object NavigateToDeleteAllCompletedScreen : TasksEvent()
        data object QuitAppPopUp : TasksEvent()
    }
}