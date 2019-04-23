package com.matheusvillela.quizandroid.di.provider

import com.matheusvillela.quizandroid.BuildConfig
import com.matheusvillela.quizandroid.shared.Api
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiProvider @Inject constructor(private val client: OkHttpClient) : Provider<Api> {

    override fun get(): Api {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(Api::class.java)
    }
}