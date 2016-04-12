package com.passenger.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.passenger.popularmovies.Constants.MOVIE_DETAILS;
import static com.passenger.popularmovies.Constants.MOVIE_POSTER;
import static com.passenger.popularmovies.Constants.MOVIE_RATING;
import static com.passenger.popularmovies.Constants.MOVIE_RELEASE_DATE;
import static com.passenger.popularmovies.Constants.MOVIE_RESULTS;
import static com.passenger.popularmovies.Constants.MOVIE_SUMMARY;
import static com.passenger.popularmovies.Constants.MOVIE_TITLE;

public class MoviesActivity extends AppCompatActivity {

    private final String TAG = MoviesActivity.class.getSimpleName();

    RecyclerView mRecyclerView;
    MoviesAdapter mAdapter;
    ArrayList<Movie> movies;
    RecyclerView.LayoutManager mLayoutManager;
    int currentSortChoice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movies);
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_list);
        setupRecyclerView();
        setupSpinner();
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        try {if (isNetworkAvailable()) {
            FetchMovies();
        } else {

            Toast.makeText(getApplicationContext(), "Please enable Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void setupSpinner() {
        Spinner sortMovies = (Spinner) findViewById(R.id.sort_movies);
        ArrayAdapter<CharSequence> sortOrderAdapter = ArrayAdapter.createFromResource(this,
                R.array.sort_choice_list, R.layout.sort_movies_list_item);
        sortOrderAdapter.setDropDownViewResource(R.layout.sort_movies_dropdown_resource);
        assert sortMovies != null;

        sortMovies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentSortChoice != position) {
                    currentSortChoice = position;
                    try {
                        if (isNetworkAvailable()) {
                            FetchMovies();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enable Internet Connection!", Toast.LENGTH_SHORT).show();

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

    private boolean isNetworkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public void setupRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movies = new ArrayList<>();
        mAdapter = new MoviesAdapter(movies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {


        Context mContext;
        private final View.OnClickListener mOnClickListener = new RecyclerViewOnClickListener();
        private ArrayList<Movie> mDataset;

        /**
         * Provide a reference to the views for each data item
         * Complex data items may need more than one view per item, and
         * you provide access to all the views for a data item in a view holder
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView poster;

            public ViewHolder(View v) {
                super(v);
                poster = (ImageView) v.findViewById(R.id.movies_list_item_image);
            }
        }

        /**
         * Add items to the adapter
         *
         * @param position
         * @param item
         */
        public void add(int position, Movie item) {
            mDataset.add(position, item);
            notifyItemInserted(position);
        }

        /**
         * Remove items from the adapter
         *
         * @param position
         */
        public void remove(int position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }

        public void swap(ArrayList<Movie> newData) {
            mDataset.clear();
            mDataset.addAll(newData);
            notifyDataSetChanged();
        }


        // Provide a suitable constructor (depends on the kind of dataset)
        public MoviesAdapter(ArrayList<Movie> moviesDataset, Context mContext) {
            mDataset = moviesDataset;
            this.mContext = mContext;

        }

        @Override
        public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, parent, false);
            view.setOnClickListener(mOnClickListener);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String imageBaseUrl = mContext.getString(R.string.image_base_url);
            final Movie movie = mDataset.get(position);
            if (movie.poster != null) {
                Log.d("Inside adapter", movie.poster);
                Picasso.with(mContext)
                        .load(imageBaseUrl + movie.poster)
                        .error(R.drawable.ic_error)
                        .into(holder.poster);


            }

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
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
                Movie userDetails = mDataset.get(position);
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                Bundle movieDetails = new Bundle();
                movieDetails.putSerializable(MOVIE_DETAILS, new Gson().toJson(userDetails));
                intent.putExtras(movieDetails);
                mContext.startActivity(intent);
            }
        }
    }

    public String setUrl() throws MalformedURLException {

        final String MOVIES_BASE_URL = getApplicationContext().getResources().
                getString(R.string.movies_base_url);
        final String SORT_PARAM = getString(R.string.sort_param_key);
        final String API_PARAM = getString(R.string.api_param_key);
        final String api_key = getApplicationContext().getResources().
                getString(R.string.api_key);
        final String sortOrder;

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

    public void FetchMovies() throws MalformedURLException {
        String url = setUrl();
        Log.d("URL", url);
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
                            Log.d(TAG, response.toString());
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
        ArrayList<Movie> fetchedMovies = new ArrayList();
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieData = movieArray.getJSONObject(i);
            fetchedMovies.add(mAdapter.getItemCount(),new Movie(
                    movieData.getString(MOVIE_TITLE),
                    movieData.getString(MOVIE_POSTER),
                    movieData.getString(MOVIE_SUMMARY),
                    movieData.getString(MOVIE_RELEASE_DATE),
                    movieData.getString(MOVIE_RATING)));
            Log.d(TAG, "Movies :" + i + "\n" + movieData.toString());
        }
        mAdapter.swap(fetchedMovies);
        Log.d(TAG, String.valueOf(fetchedMovies.size()));
        Log.d(TAG, String.valueOf(mAdapter.getItemCount()));


    }

}
