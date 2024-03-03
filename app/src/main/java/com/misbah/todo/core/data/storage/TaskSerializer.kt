package com.misbah.todo.core.data.storage

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object TaskSerializer : Serializer<TaskData> {
    override val defaultValue: TaskData = TaskData.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TaskData {
        return TaskData.parseFrom(input)
    }

    override suspend fun writeTo(t: TaskData, output: OutputStream) {
        t.writeTo(output)
    }
}