package com.matheusvillela.quizandroid.ui.question

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.matheusvillela.quizandroid.model.api.ApiAnswer
import com.matheusvillela.quizandroid.model.api.ApiAnswerResponse
import com.matheusvillela.quizandroid.model.api.ApiQuestion
import com.matheusvillela.quizandroid.shared.Api
import com.matheusvillela.quizandroid.util.Constants
import com.matheusvillela.quizandroid.util.addTo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS


class QuestionViewModel @Inject constructor(
    app: Application,
    private val api: Api
) : AndroidViewModel(app) {
    private val disposables = CompositeDisposable()
    private val stateSubject: BehaviorSubject<QuestionState> =
        BehaviorSubject.createDefault(QuestionState.InitialLoading)
    val stateObservable: Observable<QuestionState> = stateSubject
    private val answerErrorSubject = PublishSubject.create<Unit>()
    val answerErrorObservable = answerErrorSubject

    init {
        firstQuestion()
    }

    fun firstQuestion() {
        if (!(stateSubject.value is QuestionState.InitialLoading || stateSubject.value is QuestionState.InitialError)) {
            throw IllegalStateException()
        }
        api.question()
            .subscribeOn(Schedulers.io())
            .subscribe({ apiQuestion ->
                stateSubject.onNext(QuestionState.OpenQuestion(0, 0, apiQuestion))
            }, { stateSubject.onNext(QuestionState.InitialError) })
            .addTo(disposables)
    }

    fun answerCurrentQuestion(apiQuestion: ApiQuestion, option: String) {
        val currentState = stateSubject.value ?: throw IllegalStateException()
        if (!(currentState is QuestionState.OpenQuestion || currentState is QuestionState.AnsweredQuestionWithResult)) throw IllegalStateException()
        stateSubject.onNext(
            QuestionState.AnsweredQuestionLoading(
                currentState.correctAnswers,
                currentState.totalAnswers,
                apiQuestion
            )
        )
        Single
            .zip(
                Single.timer(1, TimeUnit.SECONDS),
                api.answer(apiQuestion.id, ApiAnswer(option)),
                BiFunction<Long, ApiAnswerResponse, ApiAnswerResponse> { _, response -> response }
            )
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                val totalAnswers = currentState.totalAnswers + 1
                val state = QuestionState.AnsweredQuestionWithResult(
                    currentState.correctAnswers + if (response.result) 1 else 0,
                    totalAnswers,  response.result, totalAnswers == Constants.TOTAL_ANSWERS
                )
                stateSubject.onNext(state)
            }, { answerErrorSubject.onNext(Unit) })
            .addTo(disposables)
    }

    fun nextQuestion() {
        val currentState =
            (stateSubject.value ?: throw IllegalStateException()) as? QuestionState.AnsweredQuestionWithResult
                ?: throw IllegalStateException()
        api.question()
            .subscribeOn(Schedulers.io())
            .subscribe({ apiQuestion ->
                stateSubject.onNext(
                    QuestionState.OpenQuestion(
                        currentState.correctAnswers,
                        currentState.totalAnswers,
                        apiQuestion
                    )
                )
            }, { stateSubject.onNext(QuestionState.InitialError) })
            .addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}