package com.matheusvillela.quizandroid.di.provider

import com.matheusvillela.quizandroid.BuildConfig
import com.matheusvillela.quizandroid.util.SleepInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider


class OkHttpProvider @Inject constructor(
    private val loggingInterceptor: HttpLoggingInterceptor,
    private val sleepInterceptor: SleepInterceptor
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
            //builder.addInterceptor(sleepInterceptor)
        }
        return builder.build()
    }
}
