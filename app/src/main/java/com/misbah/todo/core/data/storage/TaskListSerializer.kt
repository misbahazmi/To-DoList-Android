package com.misbah.todo.core.data.storage

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object TaskListSerializer : Serializer<TaskList> {
    override val defaultValue: TaskList = TaskList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TaskList {
        return TaskList.parseFrom(input)
    }

    override suspend fun writeTo(t: TaskList, output: OutputStream) {
        t.writeTo(output)
    }
}