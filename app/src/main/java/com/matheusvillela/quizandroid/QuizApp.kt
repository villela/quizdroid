package com.matheusvillela.quizandroid

import android.app.Application
import com.matheusvillela.quizandroid.di.module.AppModule
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

open class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
        MemberInjectorRegistryLocator.setRootRegistry(com.matheusvillela.quizandroid.MemberInjectorRegistry())
        FactoryRegistryLocator.setRootRegistry(com.matheusvillela.quizandroid.FactoryRegistry())

        val appScope: Scope = Toothpick.openScope(this)
        appScope.installModules(AppModule(this))

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}