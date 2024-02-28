package com.misbah.todo.core.data.remote

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
data class APIResult<out T>(val status: Status, val data: T?, val message: String?){

    enum class Status{
        SUCCESS, ERROR, LOADING
    }

    companion object{
        fun <T> success(data: T): APIResult<T> = APIResult(status = Status.SUCCESS, data, message = null)

        fun <T> error(message: String, data: T? = null): APIResult<T> = APIResult(status = Status.ERROR, data, message = message)

        fun <T> loading(data: T? = null): APIResult<T> = APIResult(status = Status.LOADING, data = data, message = null)
    }
}
