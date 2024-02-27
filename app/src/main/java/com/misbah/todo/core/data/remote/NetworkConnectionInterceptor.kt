package com.misbah.todo.core.data.remote

import com.misbah.todo.ui.utils.NoInternetException
import com.misbah.todo.ui.utils.Utils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * @author: Mohammad Misbah
 * @since: 26-Sep-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class NetworkConnectionInterceptor @Inject constructor(val utils: Utils): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!utils.isNetworkAvailable())
            throw NoInternetException("Make sure you have an active internet connection.")
        return chain.proceed(chain.request())
    }
}