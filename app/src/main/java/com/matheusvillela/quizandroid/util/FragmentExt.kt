package com.matheusvillela.quizandroid.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.matheusvillela.quizandroid.di.factory.ViewModelFactory

inline fun <reified T : ViewModel> Fragment.obtainViewModel() = lazy {
    ViewModelProviders.of(this, ViewModelFactory.getInstance(this.requireActivity().application)).get(T::class.java)
}