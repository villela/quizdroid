package com.matheusvillela.quizandroid.ui.name

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class NameViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val textSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("")
    private val canProceedSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private val proceedSubject: SingleSubject<String> = SingleSubject.create()

    /**
     * https://youtrack.jetbrains.com/issue/KT-14663
     * https://github.com/matejdro/KEEP/blob/private_public_property_types/proposals/private_public_property_types.md
     **/
    val canProceedObservable: Observable<Boolean> = canProceedSubject
    val proceedSingle: SingleSubject<String> = proceedSubject

    fun updateText(it: CharSequence?) {
        val text = it?.toString() ?: ""
        textSubject.onNext(text)
        canProceedSubject.onNext(!text.isBlank())
    }

    fun proceed() {
        if (canProceedSubject.value != true) throw IllegalStateException("cannot proceed")
        textSubject.value?.let {
            proceedSubject.onSuccess(it)
        }
    }
}