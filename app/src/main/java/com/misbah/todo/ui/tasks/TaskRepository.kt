package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Query
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.core.data.storage.DataStoreProvider.taskListProtoDataStore
import com.misbah.todo.core.data.storage.SortOrder
import com.misbah.todo.core.data.storage.TaskData
import com.misbah.todo.core.data.storage.TaskList
import com.nytimes.utils.AppEnums
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val context: Context) {
    private val taskDataStore = context.taskListProtoDataStore
    suspend fun saveTask(todo: ToDo) {
        val task = TaskData.newBuilder()
            .setTaskId(todo.id)
            .setTaskTitle(todo.title)
            .setTaskTodo(todo.name)
            .setTaskUserId(1)
            .setTaskStatus(todo.completed)
            .setTaskDate(todo.createdLong)
            .setTaskDuDate(todo.createdLong)
            .setTaskCat(todo.categoryValue)
            .setTaskCatDisplay(todo.category)
            .setTaskPriority(todo.priorityValue)
            .setTaskPriorityDisplay(todo.important)
            .build()

        taskDataStore.updateData { taskList ->
            taskList.toBuilder().addTasks(0,task).build()
        }
    }

    private suspend fun saveTaskList(list: MutableList<TaskData>) {
        taskDataStore.updateData { taskList ->
            taskList.toBuilder().addAllTasks(list).build()
        }
    }

    suspend fun deleteTask(taskIdToDelete: Int, currentTaskList: List<TaskData>?) {

        // Find the index of the task to delete
        val indexToDelete = currentTaskList?.indexOfFirst { it.taskId == taskIdToDelete }

        if (indexToDelete != -1) {
            // Remove the task from the list
            val updatedTaskList = currentTaskList?.toMutableList()
            updatedTaskList?.removeAt(indexToDelete!!)

            // Save the updated list of tasks
            updatedTaskList?.let {
                saveTaskList(it)
            }
        }
    }

    fun getTasksByCategory(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
        category: Int
    ): Flow<List<ToDo>> {
        return when (sortOrder) {
            SortOrder.BY_DATE -> getTasksSortByDate(category, query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortByName(category, query, hideCompleted)
            SortOrder.BY_PRIORITY -> getTasksSortByPriority(category, query, hideCompleted)
        }
    }

    fun getRemainingTask(hideCompleted: Boolean, current : Long, future : Long): Flow<List<ToDo>> {
        val list = taskDataStore.data.map { taskList ->
            taskList.tasksList.filter { it.taskDuDate in (current + 1)..<future && (it.taskStatus != hideCompleted || !hideCompleted) }
        }
        list.map { it.sortedByDescending { resul -> resul.taskDuDate } }
        return getTasks(list)
    }
    private fun getTasksSortByName(
        category: Int,
        query: String,
        hideCompleted: Boolean
    ): Flow<List<ToDo>> {
        val list = taskDataStore.data.map { taskList ->
            if (AppEnums.TasksCategory.General.value == category)
                taskList.tasksList.filter {
                    (it.taskStatus != hideCompleted || !hideCompleted) &&  (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query))
                }
            else
                taskList.tasksList.filter {
                    (it.taskStatus != hideCompleted || !hideCompleted) && it.taskCat == category &&
                            (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query))
                }
        }
        val resultList = list.map { it.sortedBy { resul -> resul.taskTitle } }
        return getTasks(resultList)
    }

    private fun getTasksSortByDate(
        category: Int,
        query: String,
        hideCompleted: Boolean
    ): Flow<List<ToDo>> {
        val list = taskDataStore.data.map { taskList ->
            if (AppEnums.TasksCategory.General.value == category)
                taskList.tasksList.filter {  (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query)) && (it.taskStatus != hideCompleted || !hideCompleted) }
            else
                taskList.tasksList.filter { it.taskCat == category &&  (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query)) && (it.taskStatus != hideCompleted || !hideCompleted) }
        }
        val resultList =  list.map { it.sortedByDescending { resul -> resul.taskDuDate } }
        return getTasks(resultList)
    }

    private fun getTasksSortByPriority(
        category: Int,
        query: String,
        hideCompleted: Boolean
    ): Flow<List<ToDo>> {
        val list = taskDataStore.data.map { taskList ->
            if (AppEnums.TasksCategory.General.value == category)
                taskList.tasksList.filter { (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query))&& (it.taskStatus != hideCompleted || !hideCompleted) }
            else
                taskList.tasksList.filter { it.taskCat == category &&  (it.taskTitle.lowercase().contains(query) || it.taskTodo.lowercase().contains(query)) && (it.taskStatus != hideCompleted || !hideCompleted) }
        }
        val resultList =   list.map { it.sortedByDescending { resul -> resul.taskPriority } }
        return getTasks(resultList)
    }

    private fun getTasks(list: Flow<List<TaskData>>): Flow<List<ToDo>> {
        val taskDoTos = list.map { taskList ->
            taskList.map { task ->
                ToDo(
                    id = task.taskId,
                    title = task.taskTitle,
                    categoryId = task.taskCat,
                    name = task.taskTodo,
                    completed = task.taskStatus,
                    userId = task.taskUserId,
                    createdLong = task.taskDate,
                    created = "",
                    due = task.taskDuDate,
                    priority = task.taskPriority,
                    category = task.taskCatDisplay,
                    important = task.taskPriorityDisplay
                )
            }
        }
        return taskDoTos
    }

    fun getAllTasks(): Flow<List<ToDo>?> {
        val list = taskDataStore.data.map { taskList ->
            taskList.tasksList
        }
        val taskDoTos = list.map { taskList ->
            taskList?.map { task ->
                ToDo(
                    id = task.taskId,
                    title = task.taskTitle,
                    categoryId = task.taskCat,
                    name = task.taskTodo,
                    completed = task.taskStatus,
                    userId = task.taskUserId,
                    createdLong = task.taskDate,
                    created = "",
                    due = task.taskDuDate,
                    priority = task.taskPriority,
                    category = task.taskCatDisplay,
                    important = task.taskPriorityDisplay
                )
            }
        }
        return taskDoTos
    }

    suspend fun deleteTask(taskId: Int) {
        taskDataStore.updateData { taskList ->
            val indexedValue = taskList.tasksList.indexOfFirst {
                it.taskId == taskId
            }
            val updatedTaskList = taskList.toBuilder()
                .removeTasks(indexedValue)
                .build()
            updatedTaskList
        }
    }

    suspend fun deleteAllCompletedTask() {
        taskDataStore.updateData { taskList ->
            val updatedTaskList = taskList.toBuilder()
                .clearTasks()
                .addAllTasks(taskList.tasksList.filter { !it.taskStatus })
                .build()
            updatedTaskList
        }
    }

    suspend fun updateTask(todo: ToDo) {
        val updatedTask = TaskData.newBuilder()
            .setTaskId(todo.id)
            .setTaskTitle(todo.title)
            .setTaskTodo(todo.name)
            .setTaskUserId(1)
            .setTaskStatus(todo.completed)
            .setTaskDate(todo.getDateValue())
            .setTaskDuDate(todo.getDateValue())
            .setTaskCat(todo.categoryValue)
            .setTaskCatDisplay(todo.category)
            .setTaskPriority(todo.priorityValue)
            .setTaskPriorityDisplay(todo.important)
            .build()
        taskDataStore.updateData { taskList ->
            val indexedValue = taskList.tasksList.indexOfFirst {
                it.taskId == updatedTask.taskId
            }
            if(indexedValue == -1) {
                val updatedTaskList = taskList.toBuilder()
                    .setTasks(taskList.tasksCount - 1, updatedTask)
                    .build()
                updatedTaskList
            }else{
                val updatedTaskList = taskList.toBuilder()
                    .setTasks(indexedValue, updatedTask)
                    .build()
                updatedTaskList
            }
        }
    }

    suspend fun getTaskListSize(): Int {
        var size = 0
        taskDataStore.data.collect { taskList ->
            size = taskList.tasksList.size
        }
        return size
    }

}
