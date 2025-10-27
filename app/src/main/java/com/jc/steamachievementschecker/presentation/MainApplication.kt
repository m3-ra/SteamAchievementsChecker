package com.jc.steamachievementschecker.presentation

import android.app.Application
import com.jc.steamachievementschecker.BuildConfig
import com.jc.steamachievementschecker.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

private const val DEFAULT_TAG = "TESTJ"

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimberWithCustomTag()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private fun initTimberWithCustomTag() {
        if (!BuildConfig.DEBUG) return
        val tree = object : Timber.DebugTree() {
            override fun log(
                priority: Int,
                tag: String?,
                message: String,
                t: Throwable?
            ) = super.log(priority, DEFAULT_TAG, message, t)
        }
        Timber.plant(tree)
    }
}
