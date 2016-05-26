package com.passenger.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.passenger.popularmovies.R;
import com.passenger.popularmovies.app.Constants;
import com.passenger.popularmovies.fragments.MovieDetailFragment;

/**
 * Activity that shows the details of each movie poster
 * clicked in MoviesActivity
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set the back pressed on the back button to the default
        //functionality on pressing the back button of the device.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.MOVIE_DETAILS_URI, getIntent().getData());
            Log.d("arguments", getIntent().getDataString());
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail, movieDetailFragment).commit();
        }
    }

}
