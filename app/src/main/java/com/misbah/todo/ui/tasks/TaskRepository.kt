package com.misbah.todo.ui.tasks

import android.content.Context
import androidx.lifecycle.asLiveData
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.core.data.storage.DataStoreProvider.taskListProtoDataStore
import com.misbah.todo.core.data.storage.TaskData
import com.nytimes.utils.AppEnums
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TaskRepository(private val context: Context) {
    private val taskDataStore = context.taskListProtoDataStore
    suspend fun saveTask(task: TaskData) {
        taskDataStore.updateData { taskList ->
            taskList.toBuilder().addTasks(task).build()
        }
    }

    private suspend fun saveTaskList(list: MutableList<TaskData>) {
        taskDataStore.updateData { taskList ->
            taskList.toBuilder().addAllTasks(list).build()
        }
    }

    suspend fun deleteTask(taskIdToDelete: Int, currentTaskList : List<TaskData>? ) {

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

    fun getTasksByCategory(categoryId: Int): Flow<List<ToDo>> {
        val list = taskDataStore.data.map { taskList ->
            if(AppEnums.TasksCategory.General.value == categoryId)
                taskList.tasksList
            else
                taskList.tasksList.filter { it.taskCat == categoryId }
        }
        val taskDoTos = list.map { taskList ->
            taskList.map { task->
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
            taskList?.map { task->
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
}
