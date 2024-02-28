package com.nytimes.utils

/**
 * @author: MOHAMMAD MISBAH
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java||Flutter
 */
object AppEnums {

    enum class DeviceType{
        ANDROID,
        IOS
    }
    enum class TasksPriority(val value: Int) {
        Normal(0), Low(1), Medium(2), High(3)
    }

    enum class TasksCategory(val value: Int) {
        All(0), Work(1), Personal(2), Wishlist(3), Shopping(4)
    }
}