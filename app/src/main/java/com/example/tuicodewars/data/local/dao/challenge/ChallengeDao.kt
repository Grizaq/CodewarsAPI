package com.example.tuicodewars.data.local.dao.challenge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tuicodewars.data.model.challenge.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenge WHERE id = :id LIMIT 1")
    fun getChallengeById(id: String): Flow<Challenge>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Query("DELETE FROM challenge WHERE id = :id")
    suspend fun deleteChallengeById(id: String)

    @Query("DELETE FROM challenge")
    suspend fun clearAllChallenges()
}