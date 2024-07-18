package com.example.tuicodewars.data.repository

import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getItemsList(): Flow<Resource<DataJhoffner>>
    suspend fun getAuthoredList(): Flow<Resource<Authored>>
    suspend fun getChallengeData(id : String): Flow<Resource<Challenge>>
}