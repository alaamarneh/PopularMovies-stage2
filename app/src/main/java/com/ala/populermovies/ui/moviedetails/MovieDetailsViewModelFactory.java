package com.ala.populermovies.ui.moviedetails;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ala.populermovies.models.Movie;
import com.ala.populermovies.repository.DataRepository;

public class MovieDetailsViewModelFactory implements ViewModelProvider.Factory {
    private DataRepository dataRepository;
    private Movie movie;
    private Application application;

    public MovieDetailsViewModelFactory(Application application, DataRepository dataRepository, Movie movie) {
        this.dataRepository = dataRepository;
        this.movie = movie;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailsViewModel(application, dataRepository, movie);
    }
}
