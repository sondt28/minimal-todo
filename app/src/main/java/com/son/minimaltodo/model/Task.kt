package com.son.minimaltodo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String = "",
    var date: LocalDateTime,
    var isDone: Boolean = false,
    var isNotify: Boolean = true,
    val color: Int = 0xFF0000
) : Parcelable
