package com.son.minimaltodo.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimeStamp(value: String) : LocalDateTime? {
        return value.let {
            LocalDateTime.parse(it)
        }
    }

    @TypeConverter
    fun dateToTimeStamp(date: LocalDateTime?) : String? {
        return date.toString()
    }
}