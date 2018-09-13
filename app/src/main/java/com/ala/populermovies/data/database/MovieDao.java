package com.ala.populermovies.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ala.populermovies.models.Movie;

import java.util.List;

import io.reactivex.Single;


@Dao
public interface MovieDao {
    @Query("SELECT * FROM MOVIES")
    LiveData<List<Movie>> getAllFavorite();

    @Delete
    void delete(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(Movie movie);

    @Query("SELECT * FROM MOVIES WHERE id = :movieId")
    Single<List<Movie>> getMovieCount(int movieId);

}
