package com.misbah.todo.ui.tasks

import com.misbah.todo.core.data.remote.RemoteDataSource
import com.misbah.todo.core.data.remote.resultLiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

open class RemoteTaskRepository (private val remoteDataSource: RemoteDataSource, @Named("IO") private val io: CoroutineDispatcher = Dispatchers.IO){
    open fun getDefaultTasks() = resultLiveData(
        io = io,
        networkCall = {
            remoteDataSource.getDefaultTasks()
        }
    )
}

