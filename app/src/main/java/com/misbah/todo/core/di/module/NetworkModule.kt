package com.misbah.todo.core.di.module

import com.misbah.todo.BuildConfig
import com.misbah.todo.core.data.remote.APIService
import com.misbah.todo.core.data.remote.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Module
class NetworkModule {
    private val REQUEST_TIMEPUT = 10L
    private lateinit var okHttpClinet: OkHttpClient


    @Singleton
    @Provides
    fun provideClient(networkConnectionInterceptor: NetworkConnectionInterceptor) : OkHttpClient{
        val logging= HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        okHttpClinet = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEPUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEPUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(getHeaderInterceptor())
            .addInterceptor(networkConnectionInterceptor)
            .build()
        return okHttpClinet
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : APIService {
        return retrofit.create(APIService::class.java)
    }

    /**
     * @author Misbah
     */
    private fun getHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request: okhttp3.Request = chain.request()
            val headers = request.headers.newBuilder()
                .add("Accept", "application/json")
                .build()
            request = request.newBuilder().headers(headers).build()
            chain.proceed(request)
        }
    }
}