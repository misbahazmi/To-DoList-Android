package com.misbah.todo.core.di.factory

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): Worker
}