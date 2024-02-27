package com.misbah.todo.ui.dialogs
import com.misbah.todo.core.data.storage.TaskDao
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
open class DialogRepository @Inject constructor(private val taskDao : TaskDao){
    fun deleteAllCompletedTasks(){
        taskDao.deleteCompletedTasks()
    }
}