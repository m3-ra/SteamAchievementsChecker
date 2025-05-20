package com.example.steamachievementschecker.presentation

import android.app.Application
import com.example.steamachievementschecker.di.appModule
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}
