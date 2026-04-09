package com.example.eduapp.di

import androidx.room.Room
import com.example.eduapp.database.AppDatabase
import com.example.eduapp.viewmodel.AppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_db"
        ).build()
    }
    
    single { get<AppDatabase>().appDao() }
    
    viewModel { AppViewModel(get()) }
}
