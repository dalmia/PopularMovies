

package com.passenger.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.passenger.popularmovies.data.MoviesContract.MoviesEntry;


/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    //Increment the database version when database schema changed
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.

                // the ID of the movie entry associated with this movie data
                MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                //movie entry columns along with their data types
                MoviesEntry.COLUMN_TITLE_KEY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_POSTER_KEY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_SUMMARY_KEY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RELEASE_DATE_KEY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RATING_KEY + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_TAG_KEY + " TEXT NOT NULL" +
                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
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
        onCreate(sqLiteDatabase);
    }
}
