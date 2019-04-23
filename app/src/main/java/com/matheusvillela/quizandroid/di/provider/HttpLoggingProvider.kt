package com.matheusvillela.quizandroid.di.provider

import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Provider


class HttpLoggingProvider @Inject constructor() : Provider<HttpLoggingInterceptor> {

    override fun get(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }
}