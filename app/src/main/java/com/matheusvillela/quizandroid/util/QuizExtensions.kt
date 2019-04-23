package com.matheusvillela.quizandroid.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable?.addTo(compositeDisposable: CompositeDisposable) = this?.let { compositeDisposable.add(this) }
