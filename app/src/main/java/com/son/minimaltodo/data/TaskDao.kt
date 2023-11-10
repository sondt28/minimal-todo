package com.son.minimaltodo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.son.minimaltodo.model.Task

@Dao
interface TaskDao {
    @Delete
    suspend fun delete(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: Task): Long

    @Query("SELECT * FROM tasks WHERE id= :taskId")
    suspend fun getTask(taskId: Int) : Task

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll() : LiveData<List<Task>>
}