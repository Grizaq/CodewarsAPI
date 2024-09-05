package com.example.tuicodewars.data.local.rooms

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tuicodewars.data.local.dao.authored.AuthoredDao
import com.example.tuicodewars.data.local.dao.challenge.ChallengeDao
import com.example.tuicodewars.data.local.dao.user.DataJhoffnerDao
import com.example.tuicodewars.data.model.authored.Authored
import com.example.tuicodewars.data.model.challenge.Challenge
import com.example.tuicodewars.data.model.user.DataJhoffner
import com.example.tuicodewars.data.utils.converters.Converters

@Database(entities = [DataJhoffner::class, Authored::class, Challenge::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun dataJhoffnerDao(): DataJhoffnerDao
    abstract fun authoredDao(): AuthoredDao
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}