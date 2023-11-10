package com.son.minimaltodo

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.son.minimaltodo.di.appModule
import com.son.minimaltodo.di.repositoryModule
import com.son.minimaltodo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MinimalTodoApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MinimalTodoApplication)
            modules(appModule, viewModelModule, repositoryModule)
        }
    }
}