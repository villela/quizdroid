package com.matheusvillela.quizandroid.ui.name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.matheusvillela.quizandroid.R
import com.matheusvillela.quizandroid.ui.question.QuestionViewModel
import com.matheusvillela.quizandroid.util.Constants
import com.matheusvillela.quizandroid.util.addTo
import com.matheusvillela.quizandroid.util.obtainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_name.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NameFragment : Fragment() {

    private val viewModel: NameViewModel by obtainViewModel()

    private var disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            nameFragmentEditText.setText(it.getString(Constants.NAME_EXTRA))
        }
        nameFragmentProceedButton
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe { viewModel.proceed() }
            .addTo(disposables)
        nameFragmentEditText.textChanges()
            .subscribe { viewModel.updateText(it) }
            .addTo(disposables)
        viewModel.proceedSingle
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userName ->
                val action = NameFragmentDirections.actionNameFragmentToQuestionFragment(userName)
                view.findNavController().navigate(action)
            }
            .addTo(disposables)
        viewModel.canProceedObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { nameFragmentProceedButton.isEnabled = it }
            .addTo(disposables)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.NAME_EXTRA, nameFragmentEditText.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
        disposables = CompositeDisposable()
    }
}