package com.son.minimaltodo.di

import androidx.room.Room
import com.son.minimaltodo.data.TaskDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidContext(), TaskDatabase::class.java, "task_db").build()
    }
    single { get<TaskDatabase>().taskDao }
}