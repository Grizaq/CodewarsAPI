package com.example.tuicodewars.domain.repository

import android.util.Log
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.remote.API
import com.example.tuicodewars.data.repository.Repository
import com.example.tuicodewars.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryJhoffner @Inject constructor(private val api: API) : Repository {
    override suspend fun getItemsList(): Flow<Resource<DataJhoffner>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getItemList()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: run {
                    emit(Resource.Error("Empty response body"))
                }
            } else {
                emit(Resource.Error(response.code().toString()))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Could not load data"))
        } catch (e: IOException) {
            emit(Resource.Error("Check internet"))
        }
    }

    override suspend fun getAuthoredList(): Flow<Resource<Authored>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAuthoredList()

            if (response.isSuccessful) {
                response.body()?.let {
                    Log.i("DebugNetworkRepo", "Success: $it")
                    emit(Resource.Success(it))
                } ?: run {
                    Log.e("DebugNetworkRepo", "Response body is null")
                    emit(Resource.Error("Empty response body"))
                }
            } else {
                Log.e("DebugNetworkRepo", "Error: ${response.code()}")
                emit(Resource.Error(response.code().toString()))
            }
        } catch (e: HttpException) {
            Log.e("DebugNetworkRepo", "HttpException: ${e.message}")
            emit(Resource.Error("Could not load data"))
        } catch (e: IOException) {
            Log.e("DebugNetworkRepo", "IOException: ${e.message}")
            emit(Resource.Error("Check internet"))
        }
    }

    override suspend fun getChallengeData(id : String): Flow<Resource<Challenge>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getChallengeData(id = id)

            if (response.isSuccessful) {
                response.body()?.let {
//                    Log.i("DebugNetworkRepo", "Success: $it")
                    emit(Resource.Success(it))
                } ?: run {
//                    Log.e("DebugNetworkRepo", "Response body is null")
                    emit(Resource.Error("Empty response body"))
                }
            } else {
//                Log.e("DebugNetworkRepo", "Error: ${response.code()}")
                emit(Resource.Error(response.code().toString()))
            }
        } catch (e: HttpException) {
//            Log.e("DebugNetworkRepo", "HttpException: ${e.message}")
            emit(Resource.Error("Could not load data"))
        } catch (e: IOException) {
//            Log.e("DebugNetworkRepo", "IOException: ${e.message}")
            emit(Resource.Error("Check internet"))
        }
    }
}