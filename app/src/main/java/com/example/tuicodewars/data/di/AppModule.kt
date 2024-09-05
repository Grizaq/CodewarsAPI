package com.example.tuicodewars.data.di

import android.content.Context
import com.example.tuicodewars.data.local.dao.authored.AuthoredDao
import com.example.tuicodewars.data.local.dao.challenge.ChallengeDao
import com.example.tuicodewars.data.local.dao.user.DataJhoffnerDao
import com.example.tuicodewars.data.local.rooms.UserDatabase
import com.example.tuicodewars.data.remote.API
import com.example.tuicodewars.domain.repository.Repository
import com.example.tuicodewars.data.utils.Utils.BASE_URL
import com.example.tuicodewars.data.repository.RepositoryJhoffner
import com.example.tuicodewars.data.utils.NetworkCheckerImpl
import com.example.tuicodewars.domain.utils.NetworkChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(
            1,
            TimeUnit.SECONDS
        ) // Short timeout for emulator bug where API response is in loading for too long
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): API = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    @Provides
    @Singleton
    fun provideRepo(api: API, dataJhoffnerDao: DataJhoffnerDao, authoredDao: AuthoredDao, challengeDao: ChallengeDao, networkChecker: NetworkChecker): Repository =
        RepositoryJhoffner(api, dataJhoffnerDao, authoredDao, challengeDao, networkChecker)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return UserDatabase.getDatabase(context)
    }

    @Provides
    fun provideDataJhoffnerDao(database: UserDatabase): DataJhoffnerDao {
        return database.dataJhoffnerDao()
    }

    @Provides
    fun provideAuthoredDao(database: UserDatabase): AuthoredDao {
        return database.authoredDao()
    }

    @Provides
    fun provideChallengeDao(database: UserDatabase): ChallengeDao {
        return database.challengeDao()
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkCheckerImpl(context)
    }
}
