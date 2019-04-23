package com.matheusvillela.quizandroid.ui.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.matheusvillela.quizandroid.model.api.ApiQuestion
import com.matheusvillela.quizandroid.util.Constants
import com.matheusvillela.quizandroid.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_question.*
import java.util.concurrent.TimeUnit
import com.google.android.material.snackbar.Snackbar
import com.matheusvillela.quizandroid.R
import com.matheusvillela.quizandroid.util.obtainViewModel

class QuestionFragment : Fragment(), QuestionAdapter.AnswerSelectedListener {

    private val viewModel: QuestionViewModel by obtainViewModel()

    private var disposables = CompositeDisposable()
    private val questionAdapter = QuestionAdapter(this)
    private var currentQuestion: ApiQuestion? = null
    private var currentAnswer: String? = null
    private var correctAnswers: Int = 0
    private val args: QuestionFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()
        questionFragmentRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        questionFragmentRecycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        questionFragmentRecycler.adapter = questionAdapter

        savedInstanceState?.let {
            currentAnswer = it.getString(Constants.ANSWER_EXTRA)
            questionAdapter.setCurrentAnswer(currentAnswer)
        }

        questionFragmentShowResults
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val action = QuestionFragmentDirections.questionFragmentToResultsFragment(args.nickname, correctAnswers)
                view.findNavController().navigate(action)
            }
            .addTo(disposables)
        viewModel.stateObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { updateState(it) }
            .addTo(disposables)
        viewModel.answerErrorObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showErrorSnack() }
            .addTo(disposables)
        questionFragmentAnswerQuestion
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                currentQuestion?.let { question ->
                    currentAnswer?.let {
                        viewModel.answerCurrentQuestion(question, it)
                    }
                }
            }
            .addTo(disposables)
        questionFragmentErrorLoadingTextView
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.firstQuestion() }
            .addTo(disposables)
        questionFragmentNextQuestion
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.nextQuestion() }
            .addTo(disposables)
    }

    private fun showErrorSnack() {
        Snackbar.make(questionFragmentAnimator, R.string.error_sending_answer, Snackbar.LENGTH_SHORT).show()
    }

    private fun updateState(state: QuestionState) {
        val child = when (state) {
            QuestionState.InitialLoading -> 0
            QuestionState.InitialError -> 1
            is QuestionState.AnsweredQuestionLoading -> 3
            is QuestionState.AnsweredQuestionWithResult -> {
                questionFragmentCorrectAnswer.visibility = if (state.correct) View.VISIBLE else View.GONE
                questionFragmentWrongAnswer.visibility = if (state.correct) View.GONE else View.VISIBLE
                questionFragmentNextQuestion.visibility = if (state.quizFinished) View.GONE else View.VISIBLE
                questionFragmentShowResults.visibility = if (state.quizFinished) View.VISIBLE else View.GONE
                correctAnswers = state.correctAnswers
                4
            }
            is QuestionState.OpenQuestion -> {
                updateQuestion(state.currentQuestion)
                2
            }
        }
        questionFragmentAnimator.displayedChild = child
    }

    private fun updateQuestion(currentQuestion: ApiQuestion) {
        questionFragmentText.text = currentQuestion.statement
        questionAdapter.setAnswers(currentQuestion.options)
        this.currentQuestion = currentQuestion
        updateSendQuestionButton()
    }

    override fun onAnswerSelected(answer: String) {
        currentAnswer = answer
        updateSendQuestionButton()
    }

    private fun updateSendQuestionButton() {
        val enabled = currentQuestion?.let { question ->
            currentAnswer?.let { answer ->
                question.options.contains(answer)
            }
        } ?: false
        questionFragmentAnswerQuestion.isEnabled = enabled
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.ANSWER_EXTRA, currentAnswer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
        disposables = CompositeDisposable()
    }

}