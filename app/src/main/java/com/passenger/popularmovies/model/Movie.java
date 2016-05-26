package com.passenger.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom object for each movie that we fetch
 */
public class Movie implements Parcelable{

    public String title;
    public String poster;
    public String summary;
    public String releaseDate;
    public String rating;

    public Movie(String title, String poster, String summary, String releaseDate, String rating){
        this.title = title;
        this.poster = poster;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.rating = rating;

    }


    protected Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        summary = in.readString();
        releaseDate = in.readString();
        rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(summary);
        dest.writeString(releaseDate);
        dest.writeString(rating);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public static Movie fromCursor(Cursor mCursor){
        return new Movie(mCursor.getString(1),mCursor.getString(2),
                mCursor.getString(3),mCursor.getString(4),
                mCursor.getString(5));
    }
}
