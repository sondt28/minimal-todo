package com.son.minimaltodo.di

import com.son.minimaltodo.repository.TaskRepository
import com.son.minimaltodo.repository.TaskRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { TaskRepositoryImpl(get()) } bind TaskRepository::class
}