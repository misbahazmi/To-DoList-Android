package com.misbah.todo.ui.dialogs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class DialogViewModel @Inject constructor(private val repository: DialogRepository) : ViewModel() {
    fun onConfirmDeleteAllClick() =  CoroutineScope(Dispatchers.IO).launch {
        repository.deleteAllCompletedTasks()
    }
}