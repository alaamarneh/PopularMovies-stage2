package com.ala.populermovies.utilities;

import android.net.Uri;

import com.ala.populermovies.BuildConfig;
import com.ala.populermovies.models.Movie;
import com.ala.populermovies.models.MovieReview;
import com.ala.populermovies.models.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class MovieDBUtils {
    public final static String TYPE_MOST_POPULAR = "popular";
    public final static String TYPE_TOP_RATED = "top_rated";

    private static final String MOVIE_DB_URL =
            "https://api.themoviedb.org/3/movie";
    private static final String BASE_IMAGE_URL =
            "https://image.tmdb.org/t/p/w185/";
    private static final String BASE_IMAGE_URL_LARGE =
            "https://image.tmdb.org/t/p/w780/";
    private static final String URL_TRAILERS =
            "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String URL_REVIEWS =
            "https://api.themoviedb.org/3/movie/%s/reviews";

    private final static String API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;
    private final static String API_KEY_PARAM = "api_key";


    public static URL buildUrl(String type) {
        Uri builtUri = Uri.parse(MOVIE_DB_URL).buildUpon()
                .appendPath(type)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForTrailers(int movieId) {
        Uri builtUri = Uri.parse(String.format(URL_TRAILERS, movieId))
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForReviews(int movieId) {
        Uri builtUri = Uri.parse(String.format(URL_REVIEWS, movieId))
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static List<MovieTrailer> getMovieTrailersFromJson(String json) {
        final String PARAM_RESULTS = "results";
        final String PARAM_NAME = "name";
        final String PARAM_KEY = "key";
        final String PARAM_TYPE = "type";
        final String PARAM_ID = "id";

        List<MovieTrailer> trailers = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsJsonArray = jsonObject.getJSONArray(PARAM_RESULTS);
            trailers = new ArrayList<>();
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject trailerJsonObject = resultsJsonArray.getJSONObject(i);
                String id = trailerJsonObject.getString(PARAM_ID);
                String name = trailerJsonObject.getString(PARAM_NAME);
                String key = trailerJsonObject.getString(PARAM_KEY);
                String type = trailerJsonObject.getString(PARAM_TYPE);

                trailers.add(new MovieTrailer(id, key, name, type));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public static List<MovieReview> getMovieReviewsFromJson(String json) {
        final String PARAM_RESULTS = "results";
        final String PARAM_AUTHOR = "author";
        final String PARAM_CONTENT = "content";
        final String PARAM_ID = "id";

        List<MovieReview> reviews = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsJsonArray = jsonObject.getJSONArray(PARAM_RESULTS);
            reviews = new ArrayList<>();
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject trailerJsonObject = resultsJsonArray.getJSONObject(i);
                String id = trailerJsonObject.getString(PARAM_ID);
                String author = trailerJsonObject.getString(PARAM_AUTHOR);
                String content = trailerJsonObject.getString(PARAM_CONTENT);


                reviews.add(new MovieReview(id, author, content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public static List<Movie> getMoviesFromJson(String json) {
        final String PARAM_RESULTS = "results";
        final String PARAM_TITLE = "title";
        final String PARAM_POSTER_PATH = "poster_path";
        final String PARAM_BACKDROP_PATH = "backdrop_path";
        final String PARAM_OVERVIEW = "overview";
        final String PARAM_RATING = "vote_average";
        final String PARAM_DATE = "release_date";
        final String PARAM_ID = "id";

        List<Movie> movies = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray resultsJsonArray = jsonObject.getJSONArray(PARAM_RESULTS);
            movies = new ArrayList<>();
            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject movieJsonObject = resultsJsonArray.getJSONObject(i);
                String title = movieJsonObject.getString(PARAM_TITLE);
                String posterImage = BASE_IMAGE_URL + movieJsonObject.getString(PARAM_POSTER_PATH);
                String backdropImage = BASE_IMAGE_URL_LARGE + movieJsonObject.getString(PARAM_BACKDROP_PATH);
                String overview = movieJsonObject.getString(PARAM_OVERVIEW);
                float user_rating = (float) movieJsonObject.getDouble(PARAM_RATING);
                String release_date = movieJsonObject.getString(PARAM_DATE);
                int id = movieJsonObject.getInt(PARAM_ID);

                movies.add(new Movie(id, posterImage, backdropImage, title, overview, user_rating, release_date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
