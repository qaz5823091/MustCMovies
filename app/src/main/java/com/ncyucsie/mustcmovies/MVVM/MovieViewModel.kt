package com.ncyucsie.mustcmovies.MVVM

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ncyucsie.mustcmovies.AppDatabase

class MovieViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MovieRepository
    init {
        val dao = AppDatabase.getInstance(application).movieDao()
        repository = MovieRepository(dao)
    }

    fun addMovie(movie: Movie) {
        repository.insert(movie)
    }

    fun getAllMovies(): LiveData<List<Movie>> = repository.getAll()

    fun updateAllMovies(movies: MutableList<Movie>) {
        repository.deleteAll()
        movies.forEach {
            repository.insert(Movie(null, it.title, it.rating, it.comment))
        }
    }

    fun updateMovie(movie: Movie) {
        repository.update(movie)
    }

    fun getOneMovie(id: Int): LiveData<Movie> {
        return repository.getOne(id)
    }
}