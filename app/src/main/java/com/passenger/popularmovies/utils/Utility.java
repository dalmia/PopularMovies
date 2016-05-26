package com.passenger.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.passenger.popularmovies.app.Constants;

import static com.passenger.popularmovies.app.Constants.MOVIE_TAG;

/**
 * Created by aman on 25/5/16.
 */
public class Utility {

    /**
     * Function to check if the device is connected to the internet.
     * Returns true if connected
     *
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)  context.getApplicationContext().getSystemService(context.getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
    }


    public static void setMovieTag(int tag, Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(MOVIE_TAG, tag).apply();
    }

    public static int getMovieTag(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       return sharedPreferences.getInt(MOVIE_TAG, 0);
    }

}
