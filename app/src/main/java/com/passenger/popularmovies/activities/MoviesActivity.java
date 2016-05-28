package com.passenger.popularmovies.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.passenger.popularmovies.R;
import com.passenger.popularmovies.adapters.CursorRecyclerViewAdapter;
import com.passenger.popularmovies.app.AppController;
import com.passenger.popularmovies.app.Constants;
import com.passenger.popularmovies.data.MoviesContract;
import com.passenger.popularmovies.data.MoviesContract.FavouritesEntry;
import com.passenger.popularmovies.data.MoviesContract.MoviesEntry;
import com.passenger.popularmovies.fragments.MovieDetailFragment;
import com.passenger.popularmovies.helper.TextViewPlus;
import com.passenger.popularmovies.model.Movie;
import com.passenger.popularmovies.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.passenger.popularmovies.app.Constants.MOVIES_KEY;
import static com.passenger.popularmovies.app.Constants.MOVIE_ADULT;
import static com.passenger.popularmovies.app.Constants.MOVIE_BACKDROP_PATH;
import static com.passenger.popularmovies.app.Constants.MOVIE_ID;
import static com.passenger.popularmovies.app.Constants.MOVIE_POPULARITY;
import static com.passenger.popularmovies.app.Constants.MOVIE_POSTER;
import static com.passenger.popularmovies.app.Constants.MOVIE_RATING;
import static com.passenger.popularmovies.app.Constants.MOVIE_RELEASE_DATE;
import static com.passenger.popularmovies.app.Constants.MOVIE_RESULTS;
import static com.passenger.popularmovies.app.Constants.MOVIE_SUMMARY;
import static com.passenger.popularmovies.app.Constants.MOVIE_TITLE;
import static com.passenger.popularmovies.app.Constants.MOVIE_VOTE_COUNT;
import static com.passenger.popularmovies.utils.Utility.getMovieTag;

public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private final String TAG = MoviesActivity.class.getSimpleName();
    private final String DETAIL_FRAGMENT_TAG = "detail_fragment";
    private final int MOVIES_LOADER_ID = 1;
    private static final String[] MOVIE_COLUMNS = {
            MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_TITLE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_POSTER_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_SUMMARY_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_RELEASE_DATE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_RATING_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_ADULT_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_FAVOURITE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_MOVIE_ID_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_BACKDROP_PATH_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_POPULARITY_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry.COLUMN_VOTE_COUNT_KEY,
    };

    private static final String[] FAVOURITE_COLUMNS = {
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry._ID,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_TITLE_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_POSTER_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_SUMMARY_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_RELEASE_DATE_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_RATING_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_ADULT_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_MOVIE_ID_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_BACKDROP_PATH_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_POPULARITY_KEY,
            FavouritesEntry.TABLE_NAME + "." + FavouritesEntry.COLUMN_VOTE_COUNT_KEY,
    };

    private static final int COL_MOVIE_TITLE = 1;
    private static final int COL_MOVIE_POSTER = 2;
    private static final int COL_MOVIE_SUMMARY = 3;
    private static final int COL_MOVIE_RELEASE_DATE = 4;
    private static final int COL_MOVIE_RATING = 5;
    private static final int COL_MOVIE_ADULT = 6;
    private static final int COL_MOVIE_FAVOURITE = 7;
    private static final int COL_MOVIE_ID = 8;
    private static final int COL_MOVIE_BACKDROP_PATH = 9;
    private static final int COL_MOVIE_POPULARITY = 10;
    private static final int COL_MOVIE_VOTE_COUNT = 11;
    private static final int COL_FAVOURITE_MOVIE_TITLE = 1;
    private static final int COL_FAVOURITE_MOVIE_POSTER = 2;
    private static final int COL_FAVOURITE_MOVIE_SUMMARY = 3;
    private static final int COL_FAVOURITE_MOVIE_RELEASE_DATE = 4;
    private static final int COL_FAVOURITE_MOVIE_RATING = 5;
    private static final int COL_FAVOURITE_MOVIE_ADULT = 6;
    private static final int COL_FAVOURITE_MOVIE_ID = 7;
    private static final int COL_FAVOURITE_MOVIE_BACKDROP_PATH = 8;
    private static final int COL_FAVOURITE_MOVIE_POPULARITY = 9;
    private static final int COL_FAVOURITE_MOVIE_VOTE_COUNT = 10;

    String sortOrder;
    Spinner sortMovies;
    boolean mTwoPane;

    @Bind(R.id.movies_list)
    RecyclerView mRecyclerView;
    MoviesAdapter mAdapter;
    ArrayList<Movie> fetchedMovies;
    RecyclerView.LayoutManager mLayoutManager;
    //current choice to determine whether 'highest rated' is selected
    // or 'Most Popular' is selected
    int currentSortChoice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        //Necessary addition for using Butterknife
        ButterKnife.bind(this);
        //Initialise the cursor Loader
        initializeLoader();
        //Setup the toolbar as the support action bar
        setupToolbar();
        //Setup Recycler View
        setupSpinner();
        setupRecyclerView();

        currentSortChoice = getMovieTag(this);
        if (findViewById(R.id.movie_detail) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_KEY)) {
            try {
                //Check internet connectivity
                if (Utility.isConnected(MoviesActivity.this)) {
                    String sortOrder = MoviesEntry._ID + " ASC";
                    String choice;
                    Uri uri;
                    Cursor cur;
                    if (currentSortChoice == 2) {
                        uri = FavouritesEntry.buildFavouritesUri();
                        cur = getContentResolver().query(uri,
                                FAVOURITE_COLUMNS, null, null, sortOrder);
                        if (mAdapter != null) {
                            Log.d("This", "4");
                            mAdapter.swapCursor(cur);
                        }else{
                            mAdapter = new MoviesAdapter(this, cur);
                        }
                    } else {
                        Log.d("Called", "1");
                        FetchMovies();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //Restoring the arrayList values
            fetchedMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            String sortOrder = MoviesEntry._ID + " ASC";
            String choice;
            Uri uri;
            Cursor cur;
            sortMovies.setSelection(currentSortChoice);
            if (currentSortChoice == 2) {
                uri = FavouritesEntry.buildFavouritesUri();
                cur = getContentResolver().query(uri,
                        FAVOURITE_COLUMNS, null, null, sortOrder);
            } else {
                uri = MoviesEntry.buildMovieUri();
                if (currentSortChoice == 0) {
                    choice = getString(R.string.highest_rated_url_value);

                } else {
                    choice = getString(R.string.most_popular_url_value);
                }
                cur = getContentResolver().query(uri,
                        MOVIE_COLUMNS, MoviesEntry.COLUMN_TAG_KEY + " = ?", new String[]{choice}, sortOrder);
            }


            mAdapter = new MoviesAdapter(this, cur);
            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    /**
     * Initializes the cursor loader with the respective loader ID
     */
    public void initializeLoader() {
        getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Here", "True IS this");
        String sortOrder = MoviesEntry._ID + " ASC";
        String choice;
        Uri uri;
        Cursor cur;
        sortMovies.setSelection(currentSortChoice);
        if (currentSortChoice == 2) {
            uri = FavouritesEntry.buildFavouritesUri();
            cur = getContentResolver().query(uri,
                    FAVOURITE_COLUMNS, null, null, sortOrder);
            Log.d("Inside 1", "re");
        } else {
            Log.d("Inside 2", "re");
            uri = MoviesEntry.buildMovieUri();
            if (currentSortChoice == 0) {
                choice = getString(R.string.highest_rated_url_value);

            } else {
                choice = getString(R.string.most_popular_url_value);
            }
            cur = getContentResolver().query(uri,
                    MOVIE_COLUMNS, MoviesEntry.COLUMN_TAG_KEY + " = ?", new String[]{choice}, sortOrder);
        }
        Log.d("Inside 1", String.valueOf(cur.getCount()));
        Log.d("This", "5");
        mAdapter.swapCursor(cur);
    }

    /**
     * Initialize the toolbar
     */
    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movies);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
    }

    /**
     * Setting up the spinner to chose the type of movies
     * to be displayed
     */
    public void setupSpinner() {
        sortMovies = (Spinner) findViewById(R.id.sort_movies);
        //setting custom textView as spinner item
        ArrayAdapter<CharSequence> sortOrderAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_choice_list, R.layout.sort_movies_list_item);
        //setting custom textView as spinner dropdown item
        sortOrderAdapter.setDropDownViewResource(R.layout.sort_movies_dropdown_resource);
        //just to avoid warnings
        assert sortMovies != null;
        currentSortChoice = Utility.getMovieTag(this);
        sortMovies.setSelection(currentSortChoice);
        Uri uri;
        Cursor cur;
        if (currentSortChoice == 2) {
            uri = FavouritesEntry.buildFavouritesUri();
            cur = getContentResolver().query(uri,
                    FAVOURITE_COLUMNS, null, null, sortOrder);

        } else {
            String choice;
            uri = MoviesEntry.buildMovieUri();
            if (currentSortChoice == 0) {
                choice = getString(R.string.highest_rated_url_value);

            } else {
                choice = getString(R.string.most_popular_url_value);
            }
            cur = getContentResolver().query(uri,
                    MOVIE_COLUMNS, MoviesEntry.COLUMN_TAG_KEY + " = ?", new String[]{choice}, sortOrder);
        }
        if (mAdapter != null) {
            Log.d("This", "6");
            mAdapter.swapCursor(cur);
        }else{
            mAdapter = new MoviesAdapter(this, cur);
        }

        sortMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Shouldn't load if already selected one is selected again
                if (currentSortChoice != position) {
                    currentSortChoice = position;
                    try {
                        //Check internet connectivity
                        if (Utility.isConnected(MoviesActivity.this)) {
                            Utility.setMovieTag(position, MoviesActivity.this);
                            if (currentSortChoice == 2) {
                                String sortOrder = FavouritesEntry._ID + " ASC";
                                Uri uri = FavouritesEntry.buildFavouritesUri();
                                Cursor cur = getContentResolver().query(uri,
                                        FAVOURITE_COLUMNS, null, null, sortOrder);
                                Log.d("This", "1");
                                mAdapter.swapCursor(cur);
                            } else {
                                Log.d("Called", "2");
                                FetchMovies();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();

                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sortMovies.setAdapter(sortOrderAdapter);

    }

    /**
     * Setting up the recycler view by adding the layout
     * manager and adding the adapter
     */
    public void setupRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fetchedMovies = new ArrayList<>();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MoviesEntry._ID + " ASC";
        String choice = "";

        Uri uri;

        if (currentSortChoice == 2) {
            uri = FavouritesEntry.buildFavouritesUri();
            return new android.content.CursorLoader(this,
                    uri,
                    FAVOURITE_COLUMNS,
                    null,
                    null,
                    sortOrder);
        } else {
            uri = MoviesEntry.buildMovieUri();
            if (currentSortChoice == 0) {
                choice = getString(R.string.highest_rated_url_value);
            } else if (currentSortChoice == 1) {
                choice = getString(R.string.most_popular_url_value);
            }
            return new android.content.CursorLoader(this,
                    uri,
                    MOVIE_COLUMNS,
                    MoviesEntry.COLUMN_TAG_KEY + " = ?",
                    new String[]{choice},
                    sortOrder);
        }

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        Log.d("data", data.getCount() + "");
        Log.d("This", "2");
        if(currentSortChoice!=2)
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        Log.d("This", "3");
        mAdapter.swapCursor(null);
    }


    /**
     * Custom adapter to load images in the recyclerView
     */
    public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.ViewHolder> {


        Context mContext;
        private final View.OnClickListener mOnClickListener = new RecyclerViewOnClickListener();
        //private ArrayList<Movie> mDataset;
        private Cursor mCursor;

        /**
         * Provide a reference to the views for each data item
         * Complex data items may need more than one view per item, and
         * we provide access to all the views for a data item in a view holder
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.movies_list_item_image)
            public ImageView poster;
            @Bind(R.id.movies_card_view)
            public CardView moviesCardView;
            @Bind(R.id.title)
            public TextView title;
            @Bind(R.id.rating)
            public TextView rating;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        public MoviesAdapter(Context mContext, Cursor cursor) {
            //mDataset = moviesDataset;
            super(mContext, cursor);
            this.mContext = mContext;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
            view.setOnClickListener(mOnClickListener);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

            this.mCursor = cursor;
            String imageBaseUrl = mContext.getString(R.string.image_base_url);
            //mCursor.moveToPosition(position);
            Movie movie = Movie.fromCursor(cursor);
            //check if valid url is received to avoid errors.
            holder.title.setText(movie.title);
            holder.rating.setText(movie.rating + "/10");
            if (movie.poster != null) {
                Picasso.with(mContext)
                        .load(imageBaseUrl + cursor.getString(COL_MOVIE_POSTER))
                        .error(R.drawable.ic_error)
                        .into(holder.poster);
                holder.moviesCardView.setVisibility(View.VISIBLE);
            }

        }

        public class RecyclerViewOnClickListener implements View.OnClickListener {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                mCursor.moveToPosition(position);
                Log.d("id", mCursor.getString(COL_MOVIE_ID));
                String id;
                if (currentSortChoice == 2) {
                    id = mCursor.getString(COL_FAVOURITE_MOVIE_ID);
                } else {
                    id = mCursor.getString(COL_MOVIE_ID);
                }
                Uri uri = MoviesContract.MoviesEntry.buildMovieUriWithId(id);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(Constants.MOVIE_DETAILS_URI, uri);
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    movieDetailFragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail, movieDetailFragment, DETAIL_FRAGMENT_TAG).commit();

                } else {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class).setData(uri);
                    mContext.startActivity(intent);
                }

            }
        }

    }


    /**
     * Function to return url to be called to fetch the movies
     *
     * @return
     * @throws MalformedURLException
     */
    public String setUrl() throws MalformedURLException {

        final String MOVIES_BASE_URL = getApplicationContext().getResources().
                getString(R.string.movies_base_url);
        final String SORT_PARAM = getString(R.string.sort_param_key);
        final String API_PARAM = getString(R.string.api_param_key);
        final String api_key = getApplicationContext().getResources().
                getString(R.string.api_key);

        if (currentSortChoice == 0) {
            sortOrder = getString(R.string.highest_rated_url_value);
        } else {
            sortOrder = getString(R.string.most_popular_url_value);
        }

        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendQueryParameter(SORT_PARAM, sortOrder)
                .appendQueryParameter(API_PARAM, api_key)
                .build();
        URL url = new URL(builtUri.toString());
        return url.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelableArrayList(MOVIES_KEY, fetchedMovies);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * Function to fetch the movies list.
     *
     * @throws MalformedURLException
     */
    public void FetchMovies() throws MalformedURLException {

        String url = setUrl();
        String tag_string_req = getString(R.string.fetch_movies_tag);
        JSONObject credentialsJSON = new JSONObject();
        Log.d(TAG, credentialsJSON.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url,
                credentialsJSON,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //add the Movie objects to the adapter
                            getMoviesList(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

    }

    public void getMoviesList(JSONObject moviesJSON) throws JSONException {


        JSONArray movieArray = moviesJSON.getJSONArray(MOVIE_RESULTS);
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
        fetchedMovies = new ArrayList();
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieData = movieArray.getJSONObject(i);
            ContentValues movieValues = new ContentValues();
            String poster = movieData.getString(MOVIE_POSTER);
            movieValues.put(MoviesEntry.COLUMN_TITLE_KEY, movieData.getString(MOVIE_TITLE));
            movieValues.put(MoviesEntry.COLUMN_POSTER_KEY, poster.substring(1, poster.length()));
            movieValues.put(MoviesEntry.COLUMN_SUMMARY_KEY, movieData.getString(MOVIE_SUMMARY));
            movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE_KEY, movieData.getString(MOVIE_RELEASE_DATE));
            movieValues.put(MoviesEntry.COLUMN_RATING_KEY, movieData.getString(MOVIE_RATING));
            movieValues.put(MoviesEntry.COLUMN_ADULT_KEY, movieData.getString(MOVIE_ADULT));
            movieValues.put(MoviesEntry.COLUMN_MOVIE_ID_KEY, movieData.getString(MOVIE_ID));
            movieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH_KEY, movieData.getString(MOVIE_BACKDROP_PATH));
            movieValues.put(MoviesEntry.COLUMN_POPULARITY_KEY, movieData.getString(MOVIE_POPULARITY));
            movieValues.put(MoviesEntry.COLUMN_VOTE_COUNT_KEY, movieData.getString(MOVIE_VOTE_COUNT));
            movieValues.put(MoviesEntry.COLUMN_TAG_KEY, sortOrder);
            cVVector.add(movieValues);

            Log.d(TAG, "Movies :" + i + "\n" + movieValues.toString());
        }
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int inserted = this.getContentResolver().bulkInsert(MoviesEntry.CONTENT_URI, cvArray);
            Log.d(TAG, inserted + "");
            getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
        }
    }
}
