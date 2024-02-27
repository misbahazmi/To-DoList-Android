package com.misbah.todo.core.base

import com.misbah.todo.core.data.remote.APIResult
import com.nytimes.utils.AppLog
import com.misbah.todo.ui.utils.NoInternetException
import kotlinx.coroutines.CancellationException
import retrofit2.Response

/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResults(call: suspend ()->Response<T>) : APIResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return APIResult.success(body)
            }
            return error("${response.code()} : ${response.message()}")
        }
        catch (e: NoInternetException){
            return APIResult.error(message = e.message.toString())
        }
        catch (e: CancellationException){

            return error(e.message ?: e.toString())
        }
        catch (e: Exception){
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): APIResult<T> {
        AppLog.debugE(message)
        return APIResult.error(message = "Network call has failed due to the reason: $message" )
    }
}