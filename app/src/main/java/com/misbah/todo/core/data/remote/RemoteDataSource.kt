package com.misbah.todo.core.data.remote

import com.misbah.todo.core.base.BaseDataSource
import com.misbah.todo.core.data.remote.APIService
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class RemoteDataSource @Inject constructor(private val apiService: APIService) : BaseDataSource(){
    /* suspend method to access tasks data from API services
     */
    suspend fun getDefaultTasks() = getResults {
       apiService.getDefaultTasks()
    }
}