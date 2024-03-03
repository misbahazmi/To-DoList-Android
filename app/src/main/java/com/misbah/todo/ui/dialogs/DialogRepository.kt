package com.misbah.todo.ui.dialogs
import android.content.Context
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.ui.tasks.TaskRepository
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
open class DialogRepository @Inject constructor(private val taskDao : TaskDao, private val context: Context){
    private val repository = TaskRepository(context)
    suspend fun deleteAllCompletedTasks(){
        repository.deleteAllCompletedTask()
    }
}