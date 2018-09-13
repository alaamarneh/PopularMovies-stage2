package com.ala.populermovies.ui.moviedetails;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.ala.populermovies.R;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.models.MovieReview;
import com.ala.populermovies.models.MovieTrailer;
import com.ala.populermovies.repository.DataRepository;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MovieDetailsViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private Movie mMovie;
    private WeakReference<MovieDetailsNavigator> mNavigator;
    private MutableLiveData<List<MovieTrailer>> trailersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<MovieReview>> reviewsLiveData = new MutableLiveData<>();

    private final ObservableBoolean isFavorite = new ObservableBoolean(false);
    private final ObservableBoolean isTrailersLoading = new ObservableBoolean(false);
    private final ObservableBoolean isReviewsLoading = new ObservableBoolean(false);


    public MovieDetailsViewModel(Application application, DataRepository dataRepository, Movie movie) {
        super(application);
        this.dataRepository = dataRepository;
        this.mMovie = movie;
        init();
    }

    public ObservableBoolean getIsTrailersLoading() {
        return isTrailersLoading;
    }

    public ObservableBoolean getIsReviewsLoading() {
        return isReviewsLoading;
    }

    public MutableLiveData<List<MovieTrailer>> getTrailersLiveData() {
        return trailersLiveData;
    }

    public MutableLiveData<List<MovieReview>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    public ObservableBoolean getIsFavorite() {
        return isFavorite;
    }

    @BindingAdapter({"bind:isFavorite"})
    public static void setIsFavorite(FloatingActionButton floatingActionButton, boolean isFavorite) {
        if (isFavorite) {
            floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }


    private void init() {
        loadTrailers();
        loadReviews();
        isFavorite();
    }

    public void setNavigator(MovieDetailsNavigator navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    private void loadTrailers() {
        isTrailersLoading.set(true);
        dataRepository.getMovieTrailers(mMovie.getId())
                .subscribe(new SingleObserver<List<MovieTrailer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<MovieTrailer> movieTrailers) {
                        trailersLiveData.setValue(movieTrailers);
                        isTrailersLoading.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e.getMessage());
                        isTrailersLoading.set(false);

                    }
                });
    }

    private void loadReviews() {
        isReviewsLoading.set(true);
        dataRepository.getMovieReviews(mMovie.getId())
                .subscribe(new SingleObserver<List<MovieReview>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<MovieReview> movieReviews) {
                        reviewsLiveData.setValue(movieReviews);
                        isReviewsLoading.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e.getMessage());
                        isReviewsLoading.set(false);
                    }
                });
    }

    public void addOrRemoveFromFavorite(View view) {

        Completable completable;
        if (isFavorite.get()) {
            completable = dataRepository.removeMovie(getApplication().getApplicationContext(), mMovie);
            showMessage(getApplication().getString(R.string.removed_from_favorite));
        } else {
            completable = dataRepository.addMovieToFavorites(getApplication().getApplicationContext(), mMovie);
            showMessage(getApplication().getString(R.string.added_to_favorites));
        }

        completable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        isFavorite();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void isFavorite() {
        dataRepository.isFavorite(getApplication().getApplicationContext(), mMovie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        isFavorite.set(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void handleError(String error) {
        if (mNavigator != null)
            mNavigator.get().handleError(error);
    }

    private void showMessage(String msg) {
        if (mNavigator != null)
            mNavigator.get().showMessage(msg);
    }
}
