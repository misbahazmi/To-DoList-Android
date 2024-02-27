package com.misbah.todo.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.misbah.todo.core.data.model.Category
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.data.storage.PreferencesManager
import com.misbah.todo.core.data.storage.TaskDao
import com.misbah.todo.ui.tasks.TasksViewModel
import com.nytimes.utils.AppEnums
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class CategoryViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val categoryEventChannel = Channel<CategoryViewModel.CategoryEvent>()
    val categoryEvent = categoryEventChannel.receiveAsFlow()

    fun categoryList(): LiveData<ArrayList<Category>> {
        val catList = arrayListOf<Category>()
        for ((index, data) in AppEnums.TasksCategory.values().distinct().withIndex()) {
            catList.add(Category(data.value, data.name))
        }
        catList.removeAt(0)
        return MutableLiveData<ArrayList<Category>>().apply { value = catList }
    }

    fun onAddNewCategoryClick() = CoroutineScope(Dispatchers.IO).launch {
        categoryEventChannel.send(CategoryEvent.NavigateToAddCategoryDialog)
    }

    fun displayMessage(text: String) = CoroutineScope(Dispatchers.IO).launch {
       showMessage(text)
    }

    private fun showMessage(text: String) = CoroutineScope(Dispatchers.IO).launch {
        categoryEventChannel.send(CategoryEvent.ShowManageCategoryMessage(text))
    }

    sealed class CategoryEvent {
        data object NavigateToAddCategoryDialog : CategoryEvent()
        data class ShowManageCategoryMessage(val msg: String) : CategoryEvent()

    }
}