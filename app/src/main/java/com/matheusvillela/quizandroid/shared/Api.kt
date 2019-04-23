package com.matheusvillela.quizandroid.shared

import com.matheusvillela.quizandroid.model.api.ApiAnswer
import com.matheusvillela.quizandroid.model.api.ApiAnswerResponse
import com.matheusvillela.quizandroid.model.api.ApiQuestion
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @GET("question")
    fun question(): Single<ApiQuestion>

    @POST("answer")
    fun answer(
        @Query("questionId") questionId: String,
        @Body answer: ApiAnswer
    ): Single<ApiAnswerResponse>

}
