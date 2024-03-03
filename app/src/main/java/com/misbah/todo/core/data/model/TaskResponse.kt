package com.misbah.todo.core.data.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("todos" ) var todos : ArrayList<ToDo> = arrayListOf()
)
