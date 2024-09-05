package com.example.tuicodewars.data.local.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tuicodewars.data.model.user.DataJhoffner

@Dao
interface DataJhoffnerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataJhoffner(dataJhoffner: DataJhoffner)

    @Query("SELECT * FROM data_jhoffner WHERE id = :id")
    suspend fun getDataJhoffner(id: String): DataJhoffner?
}
