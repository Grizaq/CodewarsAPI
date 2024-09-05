package com.example.tuicodewars.data.local.dao.authored

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tuicodewars.data.model.authored.Authored
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthoredDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthored(authored: Authored)

    @Query("SELECT * FROM authored_table")
    fun getAuthoredById(): Flow<Authored?>

    @Query("DELETE FROM authored_table")
    suspend fun clearAuthoredTable()
}