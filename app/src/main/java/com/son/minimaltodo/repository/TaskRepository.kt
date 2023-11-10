package com.son.minimaltodo.repository

import androidx.lifecycle.LiveData
import com.son.minimaltodo.model.Task

interface TaskRepository {
    suspend fun getTask(taskId: Int) : Task

    fun getTasks() : LiveData<List<Task>>

    suspend fun upsertTask(task: Task) : Long

    suspend fun deleteTask(task: Task)
}