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
    public static final String PATH_FAVOURITES = "favourites";
    public static final String PATH_TRAILERS= "trailers";
    public static final String PATH_REVIEWS= "reviews";

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
        public static final String COLUMN_ADULT_KEY = "adult";
        public static final String COLUMN_FAVOURITE_KEY = "favourite";
        public static final String COLUMN_MOVIE_ID_KEY = "movieId";
        public static final String COLUMN_BACKDROP_PATH_KEY = "backdrop_ath";
        public static final String COLUMN_POPULARITY_KEY = "popularity";
        public static final String COLUMN_VOTE_COUNT_KEY = "vote_count";

        public static Uri buildMovieURI(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieUri(){
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildMovieUriWithId(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }


    // Inner class that defines the table contents of the trailers table
    public static final class TrailersEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailers";

        // Column names for the trailers table.
        public static final String COLUMN_NAME_KEY = "name";
        public static final String COLUMN_SIZE_KEY = "size";
        public static final String COLUMN_SOURCE_KEY = "source";
        public static final String COLUMN_TYPE_DATE_KEY = "type";
        public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

        public static Uri buildTrailerUri(){
            return CONTENT_URI.buildUpon().build();
        }
        public static Uri buildTrailersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailersUriWithId(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    // Inner class that defines the table contents of the favourites table
    public static final class FavouritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        public static final String TABLE_NAME = "favourites";

        // Column names for the favourites table.
        public static final String COLUMN_TITLE_KEY = "title";
        public static final String COLUMN_POSTER_KEY = "poster_link";
        public static final String COLUMN_SUMMARY_KEY = "summary";
        public static final String COLUMN_RELEASE_DATE_KEY = "release_date";
        public static final String COLUMN_RATING_KEY = "rating";
        public static final String COLUMN_TAG_KEY = "tag";
        public static final String COLUMN_ADULT_KEY = "adult";
        public static final String COLUMN_MOVIE_ID_KEY = "movieId";
        public static final String COLUMN_BACKDROP_PATH_KEY = "backdrop_ath";
        public static final String COLUMN_POPULARITY_KEY = "popularity";
        public static final String COLUMN_VOTE_COUNT_KEY = "vote_count";

        public static Uri buildFavouritesUri(){
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildFavouritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavouritesUriWithId(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    // Inner class that defines the table contents of the reviews table
    public static final class ReviewsEntry implements BaseColumns {

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_PAGE_KEY = "page";
        public static final String COLUMN_TOTAL_PAGE_KEY = "total_page";
        public static final String COLUMN_TOTAL_RESULTS_KEY = "total_results";
        public static final String COLUMN_ID_REVIEWS_KEY = "id_reviews";
        public static final String COLUMN_AUTHOR_KEY = "author";
        public static final String COLUMN_CONTENT_KEY = "content";
        public static final String COLUMN_URL_KEY = "url";
        public static final String COLUMN_MOVIE_ID_KEY = "id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;


        public static Uri buildReviewsUri(){
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewsUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }


    }

}