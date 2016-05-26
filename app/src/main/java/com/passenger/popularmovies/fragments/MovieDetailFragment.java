package com.passenger.popularmovies.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.passenger.popularmovies.R;
import com.passenger.popularmovies.activities.MoviesActivity;
import com.passenger.popularmovies.app.Constants;
import com.passenger.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Fragment class of MovieDetailActivity
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID = 0;
    @Bind(R.id.poster)
    ImageView poster;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.rating)
    TextView rating;

    Uri mUri;

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_TITLE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_POSTER_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_SUMMARY_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RATING_KEY,
    };
    private static final int COL_MOVIE_TITLE = 1;
    private static final int COL_MOVIE_POSTER = 2;
    private static final int COL_MOVIE_SUMMARY = 3;
    private static final int COL_MOVIE_RELEASE_DATE = 4;
    private static final int COL_MOVIE_RATING = 5;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        /*if (getActivity().getIntent() != null) {
            Bundle movieDetails = getActivity().getIntent().getExtras();
            //get the intent passed
            JSONObject movieJSON = null;
            Movie movie = null;
            try {
                movieJSON = new JSONObject(movieDetails.getSerializable(MOVIE_DETAILS).toString());
                movie = new Movie(movieJSON.getString(MOVIE_TITLE),movieJSON.getString(MOVIE_POSTER),
                        movieJSON.getString(MOVIE_SUMMARY),movieJSON.getString(MOVIE_RELEASE_DATE),
                        movieJSON.getString(MOVIE_RATING));
                String imageBaseUrl = getActivity().getString(R.string.image_base_url);
                if (movie.poster!=null) {
                    Picasso.with(getActivity())
                            .load(imageBaseUrl + movie.poster)
                            .error(R.drawable.ic_error)
                            .into(poster);
                }else{
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.addRule(RelativeLayout.BELOW, R.id.release_date);
                    rating.setLayoutParams(p);
                    RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.addRule(RelativeLayout.BELOW, R.id.rating);
                    summary.setLayoutParams(p2);
                }
                //Getting the year,month and day from the received
                //release date to format as required
                if (!movie.releaseDate.equals("")) {
                    String[] dateParameters = movie.releaseDate.split("-");
                    //Creating a new instance of calendar
                    Calendar calendar = Calendar.getInstance();
                    //Setting the calendar time to the one received as releaseDate
                    if (dateParameters.length > 0) {
                        calendar.set(Integer.valueOf(dateParameters[0]), Integer.valueOf(dateParameters[1]), Integer.valueOf(dateParameters[2]));
                        Date date = new Date(calendar.getTimeInMillis());
                        //Used to format the date as required
                        SimpleDateFormat sdf = new SimpleDateFormat("dd EEEE yyyy", Locale.US);
                        movie.releaseDate = sdf.format(date);
                        releaseDate.setText("Release Date : \n" + movie.releaseDate);
                    }
                }
                //Set the values for each view
                rating.setText("\nRating : " + movie.rating + "/10");
                title.setText(movie.title);
                summary.setText(movie.summary);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = getArguments().getParcelable(Constants.MOVIE_DETAILS_URI);
        }
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("Loader", "Create2");
        if (mUri != null) {
            Log.d("Loader", "Create");
            return new CursorLoader(getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        String imageBaseUrl = getActivity().getString(R.string.image_base_url);
        Log.d("Size", String.valueOf(data.getCount()));
        data.moveToFirst();
        String posterValue = data.getString(COL_MOVIE_POSTER);
        if (posterValue != null) {
            Picasso.with(getActivity())
                    .load(imageBaseUrl + posterValue)
                    .error(R.drawable.ic_error)
                    .into(poster);
        } else {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.release_date);
            rating.setLayoutParams(p);
            RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.rating);
            summary.setLayoutParams(p2);
        }
        //Getting the year,month and day from the received
        //release date to format as required
        String releaseDateValue = data.getString(COL_MOVIE_RELEASE_DATE);
        if (!releaseDateValue.equals("")) {
            String[] dateParameters = releaseDateValue.split("-");
            //Creating a new instance of calendar
            Calendar calendar = Calendar.getInstance();
            //Setting the calendar time to the one received as releaseDate
            if (dateParameters.length > 0) {
                calendar.set(Integer.valueOf(dateParameters[0]), Integer.valueOf(dateParameters[1]), Integer.valueOf(dateParameters[2]));
                Date date = new Date(calendar.getTimeInMillis());
                //Used to format the date as required
                SimpleDateFormat sdf = new SimpleDateFormat("dd EEEE yyyy", Locale.US);
                releaseDateValue = sdf.format(date);
                releaseDate.setText("Release Date : \n" + releaseDateValue);
            }
        }
        //Set the values for each view
        rating.setText("\nRating : " + data.getString(COL_MOVIE_RATING) + "/10");
        title.setText(data.getString(COL_MOVIE_TITLE));
        summary.setText(data.getString(COL_MOVIE_SUMMARY));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
