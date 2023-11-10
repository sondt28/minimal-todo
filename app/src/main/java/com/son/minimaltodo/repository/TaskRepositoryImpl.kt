package com.son.minimaltodo.repository

import androidx.lifecycle.LiveData
import com.son.minimaltodo.data.TaskDao
import com.son.minimaltodo.model.Task

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun upsertTask(task: Task) : Long {
        return taskDao.upsert(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    override suspend fun getTask(taskId: Int) : Task {
        return taskDao.getTask(taskId)
    }

    override fun getTasks() : LiveData<List<Task>> {
        return taskDao.getAll()
    }
}