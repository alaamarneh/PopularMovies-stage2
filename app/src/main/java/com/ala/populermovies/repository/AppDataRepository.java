package com.ala.populermovies.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.ala.populermovies.data.database.AppDatabase;
import com.ala.populermovies.data.network.WebRequest;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.models.MovieReview;
import com.ala.populermovies.models.MovieTrailer;
import com.ala.populermovies.utilities.MovieDBUtils;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public class AppDataRepository implements DataRepository {
    private static AppDataRepository instance;

    public synchronized static AppDataRepository getInstance() {
        if (instance == null)
            instance = new AppDataRepository();
        return instance;
    }

    private AppDataRepository() {
    }

    @Override
    public Single<List<Movie>> getMostPopularMovies() {
        return Single.create(emitter -> new WebRequest(MovieDBUtils.buildUrl(MovieDBUtils.TYPE_MOST_POPULAR),
                new WebRequest.NetworkCompleteListener() {
                    @Override
                    public void onSuccess(String response) {
                        List<Movie> movieList = MovieDBUtils.getMoviesFromJson(response);
                        emitter.onSuccess(movieList);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        emitter.onError(throwable);
                    }
                }));
    }

    @Override
    public Single<List<Movie>> getHighestRatedMovies() {
        return Single.create(emitter -> {
            new WebRequest(MovieDBUtils.buildUrl(MovieDBUtils.TYPE_TOP_RATED),
                    new WebRequest.NetworkCompleteListener() {
                        @Override
                        public void onSuccess(String response) {
                            List<Movie> movieList = MovieDBUtils.getMoviesFromJson(response);
                            emitter.onSuccess(movieList);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            emitter.onError(throwable);
                        }
                    });
        });
    }

    @Override
    public LiveData<List<Movie>> getFavoriteMovies(Context context) {
        return AppDatabase.getInstance(context)
                .movieDao()
                .getAllFavorite();
    }

    @Override
    public Single<List<MovieTrailer>> getMovieTrailers(int movieId) {
        return Single.create(emitter -> {
            new WebRequest(MovieDBUtils.buildUrlForTrailers(movieId),
                    new WebRequest.NetworkCompleteListener() {
                        @Override
                        public void onSuccess(String response) {
                            List<MovieTrailer> trailers = MovieDBUtils.getMovieTrailersFromJson(response);
                            emitter.onSuccess(trailers);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            emitter.onError(throwable);
                        }
                    });
        });
    }

    @Override
    public Single<List<MovieReview>> getMovieReviews(int movieId) {
        return Single.create(emitter -> {
            new WebRequest(MovieDBUtils.buildUrlForReviews(movieId),
                    new WebRequest.NetworkCompleteListener() {
                        @Override
                        public void onSuccess(String response) {
                            List<MovieReview> reviews = MovieDBUtils.getMovieReviewsFromJson(response);
                            emitter.onSuccess(reviews);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            emitter.onError(throwable);
                        }
                    });
        });
    }

    @Override
    public Completable addMovieToFavorites(Context context, Movie movie) {
        return Completable.fromAction(() -> {
            AppDatabase.getInstance(context)
                    .movieDao()
                    .addMovie(movie);
        });
    }

    @Override
    public Completable removeMovie(Context context, Movie movie) {
        return Completable.fromAction(() -> {
            AppDatabase.getInstance(context)
                    .movieDao()
                    .delete(movie);
        });
    }

    @Override
    public Single<Boolean> isFavorite(Context context, int movieId) {
        return AppDatabase.getInstance(context)
                .movieDao()
                .getMovieCount(movieId)
                .map(movies -> {
                    return movies != null && !movies.isEmpty();
                });
    }
}
