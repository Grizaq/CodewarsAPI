package com.example.tuicodewars.data.model.authored

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tuicodewars.data.utils.converters.Converters

@Entity(tableName = "data_table")
data class Data(
    @PrimaryKey val id: String,
    val description: String,
    @TypeConverters(Converters::class)
    val languages: List<String>,
    val name: String,
    val rank: Int,
    val rankName: String,
    @TypeConverters(Converters::class)
    val tags: List<String>,
    @TypeConverters(Converters::class)
    val logoUrls: List<String> = emptyList()
) {
    companion object {
        fun List<String>.toCommaSeparatedString(): String {
            return this.joinToString(separator = ", ")
        }
    }
}
