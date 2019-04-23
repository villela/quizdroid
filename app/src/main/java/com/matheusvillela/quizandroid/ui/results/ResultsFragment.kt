package com.matheusvillela.quizandroid.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.matheusvillela.quizandroid.R
import com.matheusvillela.quizandroid.ui.name.NameFragmentDirections
import com.matheusvillela.quizandroid.ui.question.QuestionFragmentArgs
import com.matheusvillela.quizandroid.util.Constants
import com.matheusvillela.quizandroid.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.view.*
import kotlinx.android.synthetic.main.fragment_results.*
import kotlinx.android.synthetic.main.fragment_results.view.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ResultsFragment : Fragment() {

    private val args: ResultsFragmentArgs by navArgs()
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultsFragmentCongratulations.text =
            getString(R.string.result_congratulations, args.nickname, args.correctAnswers, Constants.TOTAL_ANSWERS)
        resultsFragmentPlayAgain
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                val action = ResultsFragmentDirections.resultsFragmentToQuestionFragment(args.nickname)
                view.findNavController().navigate(action)
            }
            .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}