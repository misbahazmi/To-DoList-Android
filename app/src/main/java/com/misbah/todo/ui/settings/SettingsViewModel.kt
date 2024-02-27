package com.misbah.todo.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.misbah.todo.core.data.storage.PreferencesManager
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class SettingsViewModel  @Inject constructor(preferencesManager: PreferencesManager) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}