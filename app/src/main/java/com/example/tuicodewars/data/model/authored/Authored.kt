package com.example.tuicodewars.data.model.authored

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tuicodewars.data.utils.converters.Converters

@Entity(tableName = "authored_table")
data class Authored(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Primary key for the entity in the Room database
    @ColumnInfo(name = "authored_data")
    @TypeConverters(Converters::class)
    val data: List<Data>
)
