package com.misbah.todo.ui.main


import androidx.lifecycle.ViewModel
import com.misbah.todo.ui.category.CategoryViewModel
import com.misbah.todo.ui.tasks.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: MOHAMMAD MISBAH
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Android Role
 * @desc MainView Model
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java||Flutter
 */
class MainViewModel @Inject constructor() : ViewModel(){
    private val tasksEventChannel = Channel<TasksViewModel.TasksEvent>()
    private val categoryEventChannel = Channel<CategoryViewModel.CategoryEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    val categoryEvent = categoryEventChannel.receiveAsFlow()
    fun onAddNewTaskClick() = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksViewModel.TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddNewCategoryClick() = CoroutineScope(Dispatchers.IO).launch {
        categoryEventChannel.send(CategoryViewModel.CategoryEvent.NavigateToAddCategoryDialog)
    }

    fun onBackClickQuitApp() = CoroutineScope(Dispatchers.IO).launch {
        tasksEventChannel.send(TasksViewModel.TasksEvent.QuitAppPopUp)
    }
}