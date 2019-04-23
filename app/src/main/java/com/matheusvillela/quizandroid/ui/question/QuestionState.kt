package com.matheusvillela.quizandroid.ui.question

import com.matheusvillela.quizandroid.model.api.ApiQuestion

sealed class QuestionState(val correctAnswers: Int, val totalAnswers: Int, val loading : Boolean) {
    object InitialLoading : QuestionState(0, 0, true)
    object InitialError : QuestionState(0, 0, false)
    class OpenQuestion(correctAnswers: Int, totalAnswers: Int, val currentQuestion: ApiQuestion) :
        QuestionState(correctAnswers, totalAnswers, false)

    class AnsweredQuestionLoading(correctAnswers: Int, totalAnswers: Int, val currentQuestion: ApiQuestion) :
        QuestionState(correctAnswers, totalAnswers, true)

    class AnsweredQuestionWithResult(
        correctAnswers: Int,
        totalAnswers: Int,
        val correct: Boolean,
        val quizFinished : Boolean
    ) : QuestionState(correctAnswers, totalAnswers, false)
}