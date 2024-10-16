package com.example.tuicodewars.data.repository

import com.example.tuicodewars.data.local.dao.authored.AuthoredDao
import com.example.tuicodewars.data.local.dao.challenge.ChallengeDao
import com.example.tuicodewars.data.local.dao.user.DataJhoffnerDao
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.remote.API
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.domain.utils.NetworkChecker
import com.example.tuicodewars.domain.utils.Resource
import com.example.tuicodewars.domain.utils.generateLogoUrlsForLanguages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryJhoffner @Inject constructor(
    private val api: API,
    private val dataJhoffnerDao: DataJhoffnerDao,
    private val authoredDao: AuthoredDao,
    private val challengeDao: ChallengeDao,
    private val networkChecker: NetworkChecker
) : Repository {

    private val defaultId = "508f2708b3be0c0200000002"

    override suspend fun getItemsList(): Flow<Resource<DataJhoffner>> = flow {
        // Load data from local database first
        val localData = dataJhoffnerDao.getDataJhoffner(defaultId)
        val hasLocalData = localData != null

        if (hasLocalData) {
            emit(Resource.LocalData(localData))  // Emit local data first
        } else {
            emit(Resource.Loading())  // Emit loading if no local data
        }

        // Check network availability and fetch from remote if available
        if (networkChecker.isInternetAvailable()) {
            try {
                val response = api.getItemList()
                if (response.isSuccessful) {
                    response.body()?.let { remoteData ->
                        dataJhoffnerDao.insertDataJhoffner(remoteData)  // Update local data
                        emit(Resource.Success(remoteData))  // Emit updated data
                    } ?: emit(Resource.Error("Empty response body", localData))  // Error but include local data if available
                } else {
                    if (!hasLocalData) {
                        emit(Resource.Error("Remote server error: ${response.code()}"))  // Error without local fallback
                    }
                }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown HTTP error"
                if (!hasLocalData) {
                    emit(Resource.Error("Network error: $errorMessage"))  // Ensure error message format matches test
                }
            } catch (e: IOException) {
                if (!hasLocalData) {
                    emit(Resource.Error("Network failure: ${e.message}"))  // IO error without local fallback
                }
            }
        } else {
            // If no internet connection, just show the local data and notify user
            if (hasLocalData) {
                emit(Resource.LocalData(localData))
            } else {
                emit(Resource.Error("No internet connection"))
            }
        }
    }


    override suspend fun getAuthoredList(): Flow<Resource<Authored>> = flow {
        // Load data from local database first
        val localData = authoredDao.getAuthoredById().firstOrNull()
        val hasLocalData = localData != null

        if (hasLocalData) {
            emit(Resource.LocalData(localData))
        } else {
            emit(Resource.Loading())
        }

        // Try to fetch from the remote source and update local data
        if (networkChecker.isInternetAvailable()) {
            try {
                val response = api.getAuthoredList()
                if (response.isSuccessful) {
                    response.body()?.let { remoteData ->
                        // Map remote data to include logoUrls
                        val populatedDataList = remoteData.data.map { dataItem ->
                            dataItem.copy(
                                logoUrls = dataItem.languages.generateLogoUrlsForLanguages() // Populate logoUrls
                            )
                        }

                        // Create a new Authored object with populated data
                        val authoredWithLogoUrls = Authored(
                            data = populatedDataList
                        )

                        // Insert the new Authored object into the database
                        authoredDao.insertAuthored(authoredWithLogoUrls)

                        emit(Resource.Success(authoredWithLogoUrls))
                    } ?: emit(Resource.Error("Empty response body", localData))
                } else {
                    if (!hasLocalData) {
                        emit(Resource.Error("Remote server error: ${response.code()}"))
                    }
                }
            } catch (e: HttpException) {
                if (!hasLocalData) {
                    emit(Resource.Error("Network error: ${e.message}"))
                }
            } catch (e: IOException) {
                if (!hasLocalData) {
                    emit(Resource.Error("Network failure: ${e.message}"))
                }
            }
        } else {
            // If no internet connection, just show the local data and notify user
            if (hasLocalData) {
                emit(Resource.LocalData(localData))
            } else {
                emit(Resource.Error("No internet connection"))
            }
        }
    }

    override suspend fun getChallengeData(id: String): Flow<Resource<Challenge>> = flow {
        // Load data from local database first
        val localData = challengeDao.getChallengeById(id).firstOrNull()
        val hasLocalData = localData != null

        if (hasLocalData) {
            emit(Resource.LocalData(localData))
        } else {
            emit(Resource.Loading())
        }

        // Check network availability and fetch from remote if available
        if (networkChecker.isInternetAvailable()) {
            try {
                val response = api.getChallengeData(id = id)
                if (response.isSuccessful) {
                    response.body()?.let { remoteData ->
                        challengeDao.insertChallenge(remoteData)  // Save data to local database
                        emit(Resource.Success(remoteData))
                    } ?: emit(Resource.Error("Empty response body", localData))
                } else {
                    if (!hasLocalData) {
                        emit(Resource.Error("Remote server error: ${response.code()}"))
                    }
                }
            } catch (e: HttpException) {
                if (!hasLocalData) {
                    emit(Resource.Error("Network error: ${e.message}"))
                }
            } catch (e: IOException) {
                if (!hasLocalData) {
                    emit(Resource.Error("Network failure: ${e.message}"))
                }
            }
        } else {
            // If no internet connection, just show the local data and notify user
            if (hasLocalData) {
                emit(Resource.LocalData(localData))
            } else {
                emit(Resource.Error("No internet connection"))
            }
        }
    }
}