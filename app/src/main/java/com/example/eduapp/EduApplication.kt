package com.example.eduapp

import android.app.Application
import com.example.eduapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EduApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EduApplication)
            modules(appModule)
        }
    }
}
