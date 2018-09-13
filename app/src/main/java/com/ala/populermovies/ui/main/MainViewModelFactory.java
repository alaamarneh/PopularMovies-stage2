package com.ala.populermovies.ui.main;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.ala.populermovies.repository.DataRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private DataRepository dataRepository;
    private Application application;

    public MainViewModelFactory(Application application, DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(application, dataRepository);
    }
}
