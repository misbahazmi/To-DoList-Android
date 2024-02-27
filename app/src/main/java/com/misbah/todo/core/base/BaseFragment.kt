package com.misbah.todo.core.base

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerDialogFragment

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
abstract class BaseFragment<V: ViewModel> : DaggerDialogFragment() {

    private lateinit var  viewModel: V
    abstract fun getViewModel() : V
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.viewModel = getViewModel()
    }

}