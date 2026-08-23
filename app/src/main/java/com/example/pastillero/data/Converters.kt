package com.example.pastillero.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalTime

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromMomentoDia(value: String?): MomentoDia? {
        return value?.let { MomentoDia.valueOf(it) }
    }

    @TypeConverter
    fun momentoDiaToString(momento: MomentoDia?): String? {
        return momento?.name
    }
}
