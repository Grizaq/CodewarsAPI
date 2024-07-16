package com.example.tuicodewars.data.repository

import com.example.tuicodewars.data.model.DataJhoffner
import com.example.tuicodewars.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getItemsList(): Flow<Resource<DataJhoffner>>
}