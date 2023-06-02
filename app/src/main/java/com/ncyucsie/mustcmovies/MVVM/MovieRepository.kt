package com.ncyucsie.mustcmovies.MVVM

import androidx.lifecycle.LiveData

class MovieRepository(val dao: MovieDao) {
    fun getAll(): LiveData<List<Movie>> {
        return dao.getAll()
    }

    fun insert(movie: Movie) {
        dao.insert(movie)
    }

    fun delete(movie: Movie) {
        dao.delete(movie)
    }

    fun deleteAll() {
        dao.deleteAll()
    }

    fun update(movie: Movie) {
        dao.update(movie)
    }

    fun getOne(id: Int): LiveData<Movie> {
        return dao.getOne(id)
    }
}