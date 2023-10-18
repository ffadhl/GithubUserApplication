package com.fadhlalhafizh.githubuserapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavGitUsers::class], version = 1)
abstract class FavGitUsersDatabase : RoomDatabase() {
    abstract fun favGitUsersDao(): FavGitUsersDao
    companion object{
        @Volatile
        private var INSTANCE: FavGitUsersDatabase? = null

        @JvmStatic
        fun getGithubUserDatabase(context: Context): FavGitUsersDatabase {
            if (INSTANCE == null) {
                synchronized(FavGitUsersDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavGitUsersDatabase::class.java, "note_database")
                        .build()
                }
            }
            return INSTANCE as FavGitUsersDatabase
        }
    }
}