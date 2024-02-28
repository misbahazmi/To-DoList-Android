package com.misbah.todo.ui.listeners

import com.misbah.todo.core.data.model.Category

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
interface OnCategoryClickListener {
        fun onItemClick(task: Category)
        fun onItemDeleteClick(task: Category)
        fun onItemEditClick(task: Category)
        fun onCheckBoxClick(task: Category, isChecked: Boolean)
    }