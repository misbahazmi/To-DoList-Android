package com.misbah.todo.core.data.storage

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

object CategorySerializer : Serializer<Category> {
    override val defaultValue: Category = Category.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Category {
        return Category.parseFrom(input)
    }

    override suspend fun writeTo(t: Category, output: OutputStream) {
        t.writeTo(output)
    }
}