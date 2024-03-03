package com.misbah.todo.core.data.storage
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

object DataStoreProvider {
    val Context.taskProtoDataStore: DataStore<TaskData> by dataStore(
        fileName = "task_data_proto",
        serializer = TaskSerializer
    )
    val Context.taskListProtoDataStore: DataStore<TaskList> by dataStore(
        fileName = "task_list_proto",
        serializer = TaskListSerializer
    )
    val Context.categoryDataStore: DataStore<Category> by dataStore(
        fileName = "category_data_proto",
        serializer = CategorySerializer
    )
}