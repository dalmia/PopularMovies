/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.passenger.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_TAG = 101;
    static final int MOVIE_WITH_TAG_AND_POSTER = 102;

    private static final SQLiteQueryBuilder sMovieByTagQueryBuilder;

    static {
        sMovieByTagQueryBuilder = new SQLiteQueryBuilder();

        sMovieByTagQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME);
    }

    //movies.tag = ?
    private static final String sTagSelection =
            MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry.COLUMN_TAG_KEY + " = ? ";


    //movies.tag = ? AND poster_link = ?
    private static final String sTagAndPosterSelection =
            MoviesContract.MoviesEntry.TABLE_NAME +
                    "." + MoviesContract.MoviesEntry.COLUMN_TAG_KEY + " = ? AND " +
                    MoviesContract.MoviesEntry.COLUMN_POSTER_KEY + " = ? ";

    private Cursor getMovieByTag(Uri uri, String[] projection, String sortOrder) {
        String tag = MoviesContract.MoviesEntry.getTagFromUri(uri);


        String[] selectionArgs = new String[0];
        String selection = null;


        selection = sTagSelection;
        selectionArgs = new String[]{tag};

        return sMovieByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder,
                "20"
        );
    }

    private Cursor getMovieByTagAndPoster(
            Uri uri, String[] projection, String sortOrder) {
        String tag = MoviesContract.MoviesEntry.getTagFromUri(uri);
        String poster = MoviesContract.MoviesEntry.getPosterFromUri(uri);

        return sMovieByTagQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTagAndPosterSelection,
                new String[]{tag, poster},
                null,
                null,
                sortOrder,
                "20"
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the MOVIE, MOVIE_WITH_TAG, MOVIE_WITH_TAG_AND_POSTER,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIE + "/*", MOVIE_WITH_TAG);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIE + "/*/*", MOVIE_WITH_TAG_AND_POSTER);


        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_WITH_TAG_AND_POSTER:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_TAG:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case MOVIE:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie/*/*"
            case MOVIE_WITH_TAG_AND_POSTER: {
                retCursor = getMovieByTagAndPoster(uri, projection, sortOrder);
                break;
            }
            // "movie/*"
            case MOVIE_WITH_TAG: {
                retCursor = getMovieByTag(uri, projection, sortOrder);
                break;
            }
            // "movie"
            case MOVIE: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder,
                        "20"
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {

                long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MoviesEntry.buildMovieURI(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unable to delete");
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsAffected = 0;
        switch (match) {
            case MOVIE:
                rowsAffected = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unable to delete");
        }
        if (rowsAffected != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsAffected;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}