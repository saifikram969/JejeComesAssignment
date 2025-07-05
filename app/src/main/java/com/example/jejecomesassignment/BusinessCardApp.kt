package com.example.jejecomesassignment

import android.app.Application
import com.example.jejecomesassignment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BusinessCardApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BusinessCardApp)
            modules(appModule)
        }
    }
}