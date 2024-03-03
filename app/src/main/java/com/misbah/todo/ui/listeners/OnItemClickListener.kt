package com.misbah.todo.ui.listeners

import com.misbah.todo.core.data.model.ToDo


/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
interface OnItemClickListener {
        fun onItemClick(task: ToDo)
        fun onItemDeleteClick(task: ToDo)
        fun onItemEditClick(task: ToDo)
        fun onCheckBoxClick(task: ToDo, isChecked: Boolean)
    }