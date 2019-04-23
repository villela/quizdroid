package com.matheusvillela.quizandroid.di.module

import android.app.Application
import com.matheusvillela.quizandroid.di.provider.ApiProvider
import com.matheusvillela.quizandroid.di.provider.HttpLoggingProvider
import com.matheusvillela.quizandroid.di.provider.OkHttpProvider
import com.matheusvillela.quizandroid.shared.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module

class AppModule(application: Application) : Module() {

    init {
        setupBinds(application)
    }

    private fun setupBinds(application: Application) {
        this.bind(Application::class.java).toInstance(application)
        this.bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).singletonInScope()
        this.bind(HttpLoggingInterceptor::class.java).toProvider(HttpLoggingProvider::class.java)
        this.bind(Api::class.java).toProvider(ApiProvider::class.java).singletonInScope()
    }
}