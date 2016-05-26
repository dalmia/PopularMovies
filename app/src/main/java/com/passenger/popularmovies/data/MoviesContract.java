package com.passenger.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Defines table and column names for the movies database.
 */
public class MoviesContract {

    //Content Authority for the Content Provider
    public static final String CONTENT_AUTHORITY = "com.passenger.popularmovies";

    //Base URI for all the URIs the app will use
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Path referring to our movies table
    public static final String PATH_MOVIE = "movies";

    // Inner class that defines the table contents of the movies table
    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        // Column names for the movies table.
        public static final String COLUMN_TITLE_KEY = "title";
        public static final String COLUMN_POSTER_KEY = "poster_link";
        public static final String COLUMN_SUMMARY_KEY = "summary";
        public static final String COLUMN_RELEASE_DATE_KEY = "release_date";
        public static final String COLUMN_RATING_KEY = "rating";
        public static final String COLUMN_TAG_KEY = "tag";

        public static Uri buildMovieURI(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*
            Student: Fill in this buildWeatherLocation function
         */
        public static Uri buildMovieTag(String tag) {
            return CONTENT_URI.buildUpon().appendPath(tag).build();
        }

        public static Uri buildMovieTagAndPoster(String tag, String poster) {
            Log.d("a", poster);
            return  CONTENT_URI.buildUpon().appendPath(tag).appendPath(poster.substring(1,poster.length())).build();
        }

        public static String getTagFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getPosterFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }
}