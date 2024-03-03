package com.misbah.todo.core.data.storage

import android.R.bool
import android.R.string
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.google.gson.annotations.SerializedName
import com.misbah.todo.core.data.model.ToDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import java.io.IOException
import javax.inject.Inject

private const val DATA_STORE_FILE_NAME = "task_prefs.pb"
class TasksInfo @Inject constructor(private val context: Context) {

    private val Context.taskProtoDataStore: DataStore<TasksPreference> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = TaskPreferencesSerializer
    )

    //Writing to a proto data store
    suspend fun saveTaskInfo(todo: ToDo) {

    }

    //Reading employee object from a proto data store
    val taskInfo: Flow<TasksPreference> = context.taskProtoDataStore.data
        .map {
            it
        }

    //Reading employee object from a proto data store

    val todoItemsFlow  = context.taskProtoDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(TasksPreference.getDefaultInstance())
            } else {
                throw exception
            }
        }

    //Reading employee object property empName from a proto data store
    val empCategory: Flow<String> = context.taskProtoDataStore.data
        .map {
            it.taskCat
        }


    //Reading employee object property empDesignation from a proto data store
    val empDescription: Flow<String> = context.taskProtoDataStore.data
        .map {
            it.taskTodo
        }
}