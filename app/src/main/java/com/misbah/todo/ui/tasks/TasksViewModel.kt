package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.work.WorkManager
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.core.data.storage.PreferencesManager
import com.misbah.todo.core.data.storage.SortOrder
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.core.data.storage.TaskData
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class TasksViewModel
@Inject constructor(
    private val repository : RemoteTaskRepository,
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle,
    private val context: Context
)  : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery", "")
    val taskRepository = TaskRepository(context)
    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    val isFirstLaunch = preferencesFlow.map { it.isFirstLaunch }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        //taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted, filterPreferences.category)
        taskRepository.getTasksByCategory(query, filterPreferences.sortOrder, filterPreferences.hideCompleted, filterPreferences.category)
    }
    val tasks = tasksFlow.asLiveData()

    private val remoteTaskFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ){query, filterPReferences -> Pair(query, filterPReferences)}
        .flatMapLatest {(query, filterPreferences) ->
            if(filterPreferences.isFirstLaunch)
                repository.getDefaultTasks().asFlow()
            else
                flowOf()
    }

    val remoteTasks  = remoteTaskFlow.asLiveData()

    val localTasks  = taskRepository.getAllTasks().asLiveData()

    var remainingTasks: LiveData<List<ToDo>>? = null

    fun onSortOrderSelected(sortOrder: SortOrder) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onFilterCategoryClick(taskCategory: Int) = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateTaskCategory(taskCategory)
    }

    fun onFirstLaunch()  = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.updateFirstLaunch()
    }

    fun onTaskSelected(task: ToDo) = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun getTasksRemainingTask() = CoroutineScope(Dispatchers.IO).launch {
        val currentTime = System.currentTimeMillis()
        val futureTime = System.currentTimeMillis() + Constants.TASK_REMINDER_TIME_INTERVAL
        remainingTasks = taskRepository.getRemainingTask(true,currentTime,futureTime).asLiveData()
    }

    fun onTaskCheckedChanged(task: ToDo, isChecked: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        //taskDao.update(task.copy(taskStatus = isChecked))
        taskRepository.updateTask(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: ToDo) {
        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.deleteTask(task.id)
            tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
            try {
                WorkManager.getInstance(context).cancelAllWorkByTag(task.due.toString())
            } catch (e: Exception){
                e.localizedMessage?.let { AppLog.debugD(it) }
            }
        }
    }

    fun deleteTask(task: Int, list : List<TaskData>) {
        CoroutineScope(Dispatchers.IO).launch {
           taskRepository.deleteTask(task, list)
        }
    }

    fun onUndoDeleteClick(todo: ToDo) = CoroutineScope(Dispatchers.IO).launch {
        taskRepository.updateTask(todo)
    }

    fun saveRemoteTask(todo : ToDo) = CoroutineScope(Dispatchers.IO).launch {
        taskRepository.saveTask(todo)
    }

    fun getRemoteTask() = CoroutineScope(Dispatchers.IO).launch {
        taskRepository.getAllTasks()
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
        data class NavigateToEditTaskScreen(val task: ToDo) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: ToDo) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        data object NavigateToDeleteAllCompletedScreen : TasksEvent()
        data object QuitAppPopUp : TasksEvent()
    }
}