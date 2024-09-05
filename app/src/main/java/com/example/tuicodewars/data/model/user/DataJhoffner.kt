package com.example.tuicodewars.data.model.user

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_jhoffner")
data class DataJhoffner(
    @PrimaryKey val id: String,
    @Embedded val codeChallenges: CodeChallenges,
    val name: String,
    @Embedded val ranks: Ranks,
)
