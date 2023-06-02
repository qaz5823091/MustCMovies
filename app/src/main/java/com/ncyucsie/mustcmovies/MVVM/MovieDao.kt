package com.ncyucsie.mustcmovies.MVVM

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM movie")
    fun deleteAll()

    @Update
    fun update(movie: Movie)

    @Query("SELECT * FROM movie WHERE id=:id")
    fun getOne(id: Int): LiveData<Movie>
}