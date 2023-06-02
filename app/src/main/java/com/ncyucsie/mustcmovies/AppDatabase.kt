package com.ncyucsie.mustcmovies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ncyucsie.mustcmovies.MVVM.Movie
import com.ncyucsie.mustcmovies.MVVM.MovieDao

@Database(entities = [Movie::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    companion object {
        @Volatile
        var INSTANCE: AppDatabase?=null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }

            synchronized(this){
                val roomDatabaseInstance = Room.databaseBuilder(context, AppDatabase::class.java,"MCM-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = roomDatabaseInstance

                return roomDatabaseInstance
            }
        }
    }
}