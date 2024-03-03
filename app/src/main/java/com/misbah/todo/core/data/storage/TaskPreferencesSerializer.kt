package com.misbah.todo.core.data.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TaskPreferencesSerializer : Serializer<TasksPreference> {
    override val defaultValue: TasksPreference = TasksPreference.getDefaultInstance()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun readFrom(input: InputStream): TasksPreference {
        try {
            return TasksPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: TasksPreference, output: OutputStream) = t.writeTo(output)
}