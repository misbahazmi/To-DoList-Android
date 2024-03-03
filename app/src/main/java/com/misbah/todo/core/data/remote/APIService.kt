package com.misbah.todo.core.data.remote

import com.misbah.todo.core.data.model.TaskResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
interface APIService {
   //This interface is used exposing API
   @GET("970ec59d-1762-492b-90c0-2e60fa2d1bb4")
   suspend fun getDefaultTasks() : Response<TaskResponse>

}