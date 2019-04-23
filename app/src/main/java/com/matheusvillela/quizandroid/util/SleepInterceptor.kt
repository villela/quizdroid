package com.matheusvillela.quizandroid.util

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

class SleepInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(500L + Random().nextInt(2000))
        return chain.proceed(chain.request())
    }
}