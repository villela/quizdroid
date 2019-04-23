package com.matheusvillela.quizandroid

import android.app.Application
import com.matheusvillela.quizandroid.di.module.AppModule
import com.matheusvillela.quizandroid.model.api.ApiAnswer
import com.matheusvillela.quizandroid.shared.Api
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import toothpick.Scope
import toothpick.Toothpick


class ApiTest {
    private lateinit var appScope: Scope
    @After
    fun tearDown() {
        Toothpick.reset()
    }

    @Before
    fun setup() {
        appScope = Toothpick.openScope(this)
        val app: Application = Mockito.mock(Application::class.java)
        appScope.installModules(AppModule(app))
    }

    @Test
    fun testQuestionReturnsSomething() {
        val api: Api = appScope.getInstance(Api::class.java)
        api.question()
            .test()
            .assertValue { it.statement.isNotEmpty() }
    }

    @Test
    fun testAnswer() {
        val api: Api = appScope.getInstance(Api::class.java)
        api.question()
            .flatMap { api.answer(it.id, ApiAnswer(it.options.first()))}
            .test()
            .assertComplete()
    }
}