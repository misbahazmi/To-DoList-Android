package com.misbah.todo.core.di.builder


import com.misbah.todo.ui.category.AddCategoryDialogFragment
import com.misbah.todo.ui.tasks.AddEditFragmentModule
import com.misbah.todo.ui.tasks.AddEditTaskFragment
import com.misbah.todo.ui.category.CategoryFragment
import com.misbah.todo.ui.category.CategoryFragmentModule
import com.misbah.todo.ui.dialogs.ConfirmationDialogFragment
import com.misbah.todo.ui.dialogs.DialogFragmentModule
import com.misbah.todo.ui.dialogs.QuitDialogFragment
import com.misbah.todo.ui.dialogs.TimePickerFragment
import com.misbah.todo.ui.settings.SettingsFragment
import com.misbah.todo.ui.settings.SettingsFragmentModule
import com.misbah.todo.ui.tasks.TasksFragment
import com.misbah.todo.ui.tasks.TasksFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector(modules = [TasksFragmentModule::class])
    abstract fun contributeTasksFragment() : TasksFragment
    @ContributesAndroidInjector(modules = [AddEditFragmentModule::class])
    abstract fun contributeAddEditTaskFragment() : AddEditTaskFragment
    @ContributesAndroidInjector(modules = [DialogFragmentModule::class])
    abstract fun contributeDialogFragment() : QuitDialogFragment
    @ContributesAndroidInjector(modules = [DialogFragmentModule::class])
    abstract fun contributeQuitDialogFragment() : ConfirmationDialogFragment
    @ContributesAndroidInjector(modules = [DialogFragmentModule::class])
    abstract fun contributeTimePickerFragment() : TimePickerFragment
    @ContributesAndroidInjector(modules = [SettingsFragmentModule::class])
    abstract fun contributeSettingsFragment() : SettingsFragment
    @ContributesAndroidInjector(modules = [CategoryFragmentModule::class])
    abstract fun contributeCategoryFragment() : CategoryFragment
    @ContributesAndroidInjector(modules = [CategoryFragmentModule::class])
    abstract fun contributeAddCategoryDialogFragment() : AddCategoryDialogFragment
}