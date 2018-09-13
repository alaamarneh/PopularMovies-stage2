package com.ala.populermovies.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey
    private int id;
    private String posterImage;
    private String backdropImage;
    private String title;
    private String overview;
    private float userRating;
    private String releaseDate;

    public Movie() {
    }

    public Movie(int id, String posterImage, String backdropImage, String title, String overview, float user_rating, String release_date) {
        this.id = id;
        this.posterImage = posterImage;
        this.backdropImage = backdropImage;
        this.title = title;
        this.overview = overview;
        this.userRating = user_rating;
        this.releaseDate = release_date;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterImage = in.readString();
        backdropImage = in.readString();
        title = in.readString();
        overview = in.readString();
        userRating = in.readFloat();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String release_date) {
        this.releaseDate = release_date;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(posterImage);
        parcel.writeString(backdropImage);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeFloat(userRating);
        parcel.writeString(releaseDate);

    }
}
