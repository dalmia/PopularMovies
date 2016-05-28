

package com.passenger.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.passenger.popularmovies.data.MoviesContract.FavouritesEntry;
import com.passenger.popularmovies.data.MoviesContract.MoviesEntry;
import com.passenger.popularmovies.data.MoviesContract.ReviewsEntry;
import com.passenger.popularmovies.data.MoviesContract.TrailersEntry;


/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    //Increment the database version when database schema changed
    private static final int DATABASE_VERSION = 4;

    static final String DATABASE_NAME = "movie.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +

                // the ID of the movie entry associated with this movie data
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                //movie entry columns along with their data types
                MoviesEntry.COLUMN_TITLE_KEY + " TEXT , " +
                MoviesEntry.COLUMN_POSTER_KEY + " TEXT , " +
                MoviesEntry.COLUMN_SUMMARY_KEY + " TEXT , " +
                MoviesEntry.COLUMN_RELEASE_DATE_KEY + " TEXT , " +
                MoviesEntry.COLUMN_RATING_KEY + " TEXT , " +
                MoviesEntry.COLUMN_TAG_KEY + " TEXT , " +
                MoviesEntry.COLUMN_ADULT_KEY + " TEXT , " +
                MoviesEntry.COLUMN_FAVOURITE_KEY+ " TEXT , " +
                MoviesEntry.COLUMN_MOVIE_ID_KEY + " TEXT , " +
                MoviesEntry.COLUMN_BACKDROP_PATH_KEY + " TEXT , " +
                MoviesEntry.COLUMN_POPULARITY_KEY + " TEXT , " +
                MoviesEntry.COLUMN_VOTE_COUNT_KEY + " TEXT , " +
                "UNIQUE (" + MoviesEntry.COLUMN_MOVIE_ID_KEY + ") ON CONFLICT IGNORE)" ;

        final String CREATE_FAVOURITES_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +

                // the ID of the movie entry associated with this movie data
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                //movie entry columns along with their data types
                FavouritesEntry.COLUMN_TITLE_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_POSTER_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_SUMMARY_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_RELEASE_DATE_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_RATING_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_ADULT_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_MOVIE_ID_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_BACKDROP_PATH_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_POPULARITY_KEY + " TEXT , " +
                FavouritesEntry.COLUMN_VOTE_COUNT_KEY + " TEXT , " +
                "UNIQUE (" + FavouritesEntry.COLUMN_MOVIE_ID_KEY + ") ON CONFLICT IGNORE)" ;

        final String CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailersEntry.TABLE_NAME + " (" +

                // the ID of the movie entry associated with this movie data
                TrailersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                //movie entry columns along with their data types
                TrailersEntry.COLUMN_NAME_KEY + " TEXT , " +
                TrailersEntry.COLUMN_SIZE_KEY + " TEXT , " +
                TrailersEntry.COLUMN_SOURCE_KEY + " TEXT , " +
                TrailersEntry.COLUMN_TYPE_DATE_KEY + " TEXT , " +
                TrailersEntry.COLUMN_MOVIE_ID_KEY + " TEXT , " +
                "UNIQUE (" + TrailersEntry.COLUMN_SOURCE_KEY + ") ON CONFLICT IGNORE)" ;

        final String CREATE_REVIEWS_TABLE= "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewsEntry.COLUMN_PAGE_KEY+ "  TEXT," +
                ReviewsEntry.COLUMN_TOTAL_PAGE_KEY + "  TEXT," +
                ReviewsEntry.COLUMN_TOTAL_RESULTS_KEY + "  TEXT," +
                ReviewsEntry.COLUMN_ID_REVIEWS_KEY + "  TEXT," +
                ReviewsEntry.COLUMN_AUTHOR_KEY + "  TEXT ," +
                ReviewsEntry.COLUMN_CONTENT_KEY + "  TEXT," +
                ReviewsEntry.COLUMN_URL_KEY + "  TEXT," +
                ReviewsEntry.COLUMN_MOVIE_ID_KEY + "  TEXT, "+
                "UNIQUE (" + ReviewsEntry.COLUMN_URL_KEY + ") ON CONFLICT IGNORE)";


        sqLiteDatabase.execSQL(CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVOURITES_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRAILERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
