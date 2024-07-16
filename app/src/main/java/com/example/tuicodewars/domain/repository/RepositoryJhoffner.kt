package com.example.tuicodewars.domain.repository

import android.util.Log
import com.example.tuicodewars.data.model.DataJhoffner
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
            if (response.isSuccessful)
                response.body()?.let {
                    emit(Resource.Success(it))
                    Log.i("DebugNetworkARepoLet", response.toString())
                }
            else
                emit(Resource.Error(response.code().toString()))
        } catch (e: HttpException) {
            emit(Resource.Error("Could not load data"))
        } catch (e: IOException) {
            emit(Resource.Error("Check internet"))
        }
    }
}