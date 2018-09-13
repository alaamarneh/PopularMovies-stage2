package com.ala.populermovies.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.ala.populermovies.models.Movie;
import com.ala.populermovies.models.MovieReview;
import com.ala.populermovies.models.MovieTrailer;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface DataRepository {
    Single<List<Movie>> getMostPopularMovies();

    Single<List<Movie>> getHighestRatedMovies();

    LiveData<List<Movie>> getFavoriteMovies(Context context);

    Single<List<MovieTrailer>> getMovieTrailers(int movieId);

    Single<List<MovieReview>> getMovieReviews(int movieId);

    Completable addMovieToFavorites(Context context, Movie movie);

    Completable removeMovie(Context context, Movie movie);

    Single<Boolean> isFavorite(Context context, int movieId);
}
