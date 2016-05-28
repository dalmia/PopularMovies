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
import android.content.ContentUris;
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
    static final int MOVIE_WITH_ID = 101;
    static final int FAVOURITE = 102;
    static final int FAVOURITE_WITH_ID = 103;
    static final int TRAILER = 104;
    static final int TRAILER_WITH_ID = 105;
    static final int REVIEW = 106;
    static final int REVIEW_WITH_ID = 107;


    private static final SQLiteQueryBuilder sMovieQueryBuilder,sTrailerQueryBuilder,sFavouritesQueryBuilder,sReviewQueryBuilder;

    static {
        sMovieQueryBuilder = new SQLiteQueryBuilder();

        sMovieQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME);

        sTrailerQueryBuilder = new SQLiteQueryBuilder();

        sTrailerQueryBuilder.setTables(
                MoviesContract.TrailersEntry.TABLE_NAME);

        sFavouritesQueryBuilder = new SQLiteQueryBuilder();

        sFavouritesQueryBuilder.setTables(
                MoviesContract.FavouritesEntry.TABLE_NAME);

        sReviewQueryBuilder = new SQLiteQueryBuilder();

        sReviewQueryBuilder.setTables(
                MoviesContract.ReviewsEntry.TABLE_NAME);
    }


    private Cursor getMovieById(
            Uri uri, String[] projection, String sortOrder) {
        String id = MoviesContract.MoviesEntry.getIdFromUri(uri);

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_KEY + "=?",
                new String[]{id},
                null,
                null,
                sortOrder,
                "20"
        );
    }

    private Cursor getFavouritesById(
            Uri uri, String[] projection, String sortOrder) {
        String id = MoviesContract.FavouritesEntry.getIdFromUri(uri);

        return sFavouritesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID_KEY + "=?",
                new String[]{id},
                null,
                null,
                sortOrder,
                "20"
        );
    }

    private Cursor getTrailerById(
            Uri uri, String[] projection, String sortOrder) {
        String id = MoviesContract.TrailersEntry.getIdFromUri(uri);

        return sTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MoviesContract.TrailersEntry.COLUMN_MOVIE_ID_KEY + "=?",
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewById(Uri uri, String[] projection, String sortOrder) {
        String id = MoviesContract.ReviewsEntry.getIdFromUri(uri);

        return sReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID_KEY + "=?",
                new String[]{id},
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the MOVIE, MOVIE_WITH_TAG, MOVIE_WITH_TAG_AND_POSTER,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVOURITES, FAVOURITE);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_FAVOURITES + "/*", FAVOURITE_WITH_ID);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILERS, TRAILER);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_TRAILERS + "/*", TRAILER_WITH_ID);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEWS, REVIEW);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_REVIEWS + "/*", REVIEW_WITH_ID);
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
            case MOVIE_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case MOVIE:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case FAVOURITE_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case FAVOURITE:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case TRAILER_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case TRAILER:
                return MoviesContract.MoviesEntry.CONTENT_TYPE;
            case REVIEW_WITH_ID:
                return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
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
            // "movies/*"
            case MOVIE_WITH_ID: {
                retCursor = getMovieById(uri, projection, sortOrder);
                break;
            }

            // "movies"
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

            // "favourites/*"
            case FAVOURITE_WITH_ID: {
                retCursor = getFavouritesById(uri, projection, sortOrder);
                break;
            }

            // "favourites"
            case FAVOURITE: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        MoviesContract.FavouritesEntry.TABLE_NAME,
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

            // "trailers/*"
            case TRAILER_WITH_ID: {
                retCursor = getTrailerById(uri, projection, sortOrder);
                break;
            }

            // "trailers"
            case TRAILER: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        MoviesContract.TrailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            }

            // "reviews/*"
            case REVIEW_WITH_ID: {
                retCursor = getReviewById(uri, projection, sortOrder);
                break;
            }

            // "reviews"
            case REVIEW: {
                retCursor = mOpenHelper.getWritableDatabase().query(
                        MoviesContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
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
            case FAVOURITE: {

                long _id = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.FavouritesEntry.buildFavouritesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER: {

                long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.TrailersEntry.buildTrailersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {

                long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.ReviewsEntry.buildReviewsUri(_id);
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
            case MOVIE_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.MoviesEntry.TABLE_NAME, MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case FAVOURITE_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.FavouritesEntry.TABLE_NAME, MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case TRAILER_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.TrailersEntry.TABLE_NAME, MoviesContract.TrailersEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case REVIEW_WITH_ID:
                rowsDeleted = db.delete(MoviesContract.ReviewsEntry.TABLE_NAME, MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
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
            case MOVIE_WITH_ID:
                rowsAffected = db.update(MoviesContract.MoviesEntry.TABLE_NAME, values,
                        MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case FAVOURITE_WITH_ID:
                rowsAffected = db.update(MoviesContract.FavouritesEntry.TABLE_NAME, values,
                        MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case TRAILER_WITH_ID:
                rowsAffected = db.update(MoviesContract.TrailersEntry.TABLE_NAME, values,
                        MoviesContract.TrailersEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case REVIEW_WITH_ID:
                rowsAffected = db.update(MoviesContract.ReviewsEntry.TABLE_NAME, values,
                        MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID_KEY + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
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
        int returnCount = 0;
        switch (match) {
            case MOVIE:
                db.beginTransaction();
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

            case FAVOURITE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.FavouritesEntry.TABLE_NAME, null, value);
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

            case TRAILER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TrailersEntry.TABLE_NAME, null, value);
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
            case REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, value);
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