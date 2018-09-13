package com.ala.populermovies.ui.moviedetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ala.populermovies.R;
import com.ala.populermovies.databinding.ActivityMovieDetailsBinding;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.repository.RepoFactory;
import com.ala.populermovies.utilities.AppUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsNavigator {

    public final static String ARG_MOVIE = "movie";

    private ActivityMovieDetailsBinding activityMovieDetailsBinding;
    private MovieDetailsViewModel mViewModel;
    private Snackbar mSnackbar;
    private Movie mMovie;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        setSupportActionBar(activityMovieDetailsBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if (intent != null)
            mMovie = getIntent().getParcelableExtra(ARG_MOVIE);

        if (mMovie == null) {
            finish();
            Toast.makeText(this, R.string.failed_loading, Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel = ViewModelProviders
                .of(this, new MovieDetailsViewModelFactory(getApplication(), RepoFactory.getDataRepository(), mMovie))
                .get(MovieDetailsViewModel.class);
        mViewModel.setNavigator(this);
        activityMovieDetailsBinding.setViewModel(mViewModel);
        activityMovieDetailsBinding.content.setViewModel(mViewModel);

        activityMovieDetailsBinding.content.trailersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activityMovieDetailsBinding.content.trailersRv.setHasFixedSize(true);

        activityMovieDetailsBinding.content.reviewsRv.setLayoutManager(new LinearLayoutManager(this));
        activityMovieDetailsBinding.content.reviewsRv.setHasFixedSize(true);

        mViewModel.getReviewsLiveData()
                .observe(this, response -> {
                    reviewsAdapter = new ReviewsAdapter(response);
                    activityMovieDetailsBinding.content.reviewsRv.setAdapter(reviewsAdapter);
                });
        mViewModel.getTrailersLiveData()
                .observe(this, response -> {
                    trailersAdapter = new TrailersAdapter(response);
                    activityMovieDetailsBinding.content.trailersRv.setAdapter(trailersAdapter);
                });
        Picasso.with(this)
                .load(mMovie.getBackdropImage())
                .into(activityMovieDetailsBinding.posterImg);
        activityMovieDetailsBinding.content.nameTv.setText(mMovie.getTitle());
        activityMovieDetailsBinding.content.ratingRb.setRating(mMovie.getUserRating());
        activityMovieDetailsBinding.content.ratingTv.setText(String.format(getString(R.string.rating_text), String.valueOf(mMovie.getUserRating())));
        activityMovieDetailsBinding.content.releaseTv.setText(mMovie.getReleaseDate());
        activityMovieDetailsBinding.content.overviewTv.setText(mMovie.getOverview());
        activityMovieDetailsBinding.addToFavoriteFab.setOnClickListener(view -> {
            setResult(RESULT_OK);
            mViewModel.addOrRemoveFromFavorite(view);
        });

        setTitle("");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                shareIt();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareIt() {
        if (trailersAdapter == null) return;
        String text = trailersAdapter.getFirstYoutubeUrl(getApplicationContext());
        if (text == null) return;
        AppUtils.shareText(this, text, getString(R.string.share_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public void handleError(String error) {
        if (mSnackbar != null) mSnackbar.dismiss();
        mSnackbar = Snackbar.make(activityMovieDetailsBinding.coordinator, error, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    @Override
    public void showMessage(String message) {
        if (mSnackbar != null) mSnackbar.dismiss();
        mSnackbar = Snackbar.make(activityMovieDetailsBinding.coordinator, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }
}
