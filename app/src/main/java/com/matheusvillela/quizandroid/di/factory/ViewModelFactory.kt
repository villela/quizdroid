package com.matheusvillela.quizandroid.di.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.internal.Internal.instance
import toothpick.Toothpick
import javax.inject.Inject

class ViewModelFactory private constructor(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            Toothpick.openScope(application).getInstance(modelClass) as T

    companion object {
        @Volatile private var instance: ViewModelFactory? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(ViewModelFactory::class.java) {
                instance ?: ViewModelFactory(application)
                    .also { instance = it }
            }
    }
}