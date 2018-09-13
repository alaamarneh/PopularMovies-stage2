package com.ala.populermovies.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.ala.populermovies.R;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.repository.DataRepository;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainViewModel extends AndroidViewModel {

    public static final int TYPE_MOST_POPULAR = 0;
    public static final int TYPE_TOP_RATED = 1;
    public static final int TYPE_FAVORITE = 2;
    private DataRepository dataRepository;


    private ObservableField<String> title = new ObservableField<>();

    public MainViewModel(Application application, DataRepository dataRepository) {
        super(application);
        this.dataRepository = dataRepository;
        loadMostPopular();
    }

    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private ObservableBoolean showError = new ObservableBoolean(false);
    private int SHOW_TYPE;

    public ObservableField<String> getTitle() {
        return title;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }

    public void setShowError(boolean showError) {
        this.showError.set(showError);
    }

    public void setError(String error) {
        this.error.set(error);
        this.setShowError(true);
        this.setIsLoading(false);
    }

    private ObservableField<String> error = new ObservableField<>();
    private MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Movie>> getMoviesLiveData() {
        return moviesLiveData;
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public ObservableBoolean getShowError() {
        return showError;
    }

    public ObservableField<String> getError() {
        return error;
    }


    public void loadMostPopular() {
        SHOW_TYPE = TYPE_MOST_POPULAR;
        title.set(getApplication().getResources().getString(R.string.most_popular));
        setIsLoading(true);
        setShowError(false);
        dataRepository.getMostPopularMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Movie> movies) {
                        moviesLiveData.setValue(movies);
                        setIsLoading(false);
                        if (movies == null || movies.isEmpty())
                            setError(getApplication().getString(R.string.no_movies_available));
                    }

                    @Override
                    public void onError(Throwable e) {
                        setError(e.getMessage());
                    }
                });
    }

    public void loadHighestRated() {
        SHOW_TYPE = TYPE_TOP_RATED;
        title.set(getApplication().getResources().getString(R.string.highest_rated_movies));
        setIsLoading(true);
        setShowError(false);
        dataRepository.getHighestRatedMovies()
                .subscribe(new SingleObserver<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Movie> movies) {
                        setIsLoading(false);
                        moviesLiveData.setValue(movies);
                        if (movies == null || movies.isEmpty())
                            setError(getApplication().getString(R.string.no_movies_available));
                    }

                    @Override
                    public void onError(Throwable e) {
                        setError(e.getMessage());
                    }
                });

    }

    public void loadFavoriteMovies() {
        SHOW_TYPE = TYPE_FAVORITE;
        title.set(getApplication().getResources().getString(R.string.favorite));
        setIsLoading(true);
        setShowError(false);
        dataRepository.getFavoriteMovies(getApplication().getApplicationContext())
                .observeForever(movies -> {
                    if (SHOW_TYPE != TYPE_FAVORITE) return;
                    setIsLoading(false);
                    moviesLiveData.setValue(movies);
                    if (movies == null || movies.isEmpty())
                        setError(getApplication().getString(R.string.no_movies_available));
                });
    }

    public void notifyFavoriteChanged() {
        if (SHOW_TYPE == TYPE_FAVORITE) {
            loadFavoriteMovies();
        }
    }


}
