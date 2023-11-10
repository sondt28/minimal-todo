package com.son.minimaltodo.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.ui.add.AddViewModel
import com.son.minimaltodo.ui.home.HomeViewModel
import com.son.minimaltodo.ui.reminder.ReminderViewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val viewModelModule = module {
    factory { (task: Task) -> ReminderViewModel(task, get()) }
    factory { (task: Task) -> AddViewModel(task, get()) }
    factory { HomeViewModel(get()) }
}