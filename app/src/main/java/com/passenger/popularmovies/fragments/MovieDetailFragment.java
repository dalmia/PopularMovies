package com.passenger.popularmovies.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.passenger.popularmovies.R;
import com.passenger.popularmovies.adapters.CursorRecyclerViewAdapter;
import com.passenger.popularmovies.app.AppController;
import com.passenger.popularmovies.app.Constants;
import com.passenger.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.passenger.popularmovies.app.Constants.MOVIE_FAVOURITE;
import static com.passenger.popularmovies.app.Constants.MOVIE_ID;


/**
 * Fragment class of MovieDetailActivity
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int LOADER_ID = 0;
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    @Bind(R.id.poster)
    ImageView poster;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.trailer_text)
    TextView trailerText;
    @Bind(R.id.review_text)
    TextView reviewText;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.rating)
    TextView rating;
    @Bind(R.id.trailers)
    RecyclerView trailers;
    @Bind(R.id.reviews)
    RecyclerView reviews;
    @Bind(R.id.fav_movie)
    FloatingActionButton favMovie;
    boolean favourite;
    String movieId;
    ContentValues movieValues;
    LinearLayoutManager trailerLayoutManager, reviewLayoutManager;
    TrailerAdapter trailerMovieAdapter;
    ReviewAdapter reviewMovieAdapter;
    Uri mUri, trailersUri, reviewsUri;

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_TITLE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_POSTER_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_SUMMARY_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_RATING_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_ADULT_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_FAVOURITE_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_POPULARITY_KEY,
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT_KEY,
    };

    private static final String[] FAVOURITE_COLUMNS = {
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry._ID,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_TITLE_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_POSTER_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_SUMMARY_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_RATING_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_ADULT_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_BACKDROP_PATH_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_POPULARITY_KEY,
            MoviesContract.FavouritesEntry.TABLE_NAME + "." + MoviesContract.FavouritesEntry.COLUMN_VOTE_COUNT_KEY,
    };

    private static final String[] TRAILER_COLUMNS = {
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry._ID,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_NAME_KEY,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_SIZE_KEY,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_SOURCE_KEY,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_TYPE_DATE_KEY,
            MoviesContract.TrailersEntry.TABLE_NAME + "." + MoviesContract.TrailersEntry.COLUMN_MOVIE_ID_KEY,
    };

    private static final String[] REVIEW_COLUMNS = {
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry._ID,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_PAGE_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_TOTAL_PAGE_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_TOTAL_RESULTS_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_ID_REVIEWS_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_AUTHOR_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_CONTENT_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_URL_KEY,
            MoviesContract.ReviewsEntry.TABLE_NAME + "." + MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID_KEY,
    };

    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_POSTER = 2;
    public static final int COL_MOVIE_SUMMARY = 3;
    public static final int COL_MOVIE_RELEASE_DATE = 4;
    public static final int COL_MOVIE_RATING = 5;
    public static final int COL_MOVIE_ADULT = 6;
    public static final int COL_MOVIE_FAVOURITE = 7;
    public static final int COL_MOVIE_ID = 8;
    public static final int COL_MOVIE_BACKDROP_PATH = 9;
    public static final int COL_MOVIE_POPULARITY = 10;
    public static final int COL_MOVIE_VOTE_COUNT = 11;
    public static final int COL_FAVOURITE_MOVIE_TITLE = 1;
    public static final int COL_FAVOURITE_MOVIE_POSTER = 2;
    public static final int COL_FAVOURITE_MOVIE_SUMMARY = 3;
    public static final int COL_FAVOURITE_MOVIE_RELEASE_DATE = 4;
    public static final int COL_FAVOURITE_MOVIE_RATING = 5;
    public static final int COL_FAVOURITE_MOVIE_ADULT = 6;
    public static final int COL_FAVOURITE_MOVIE_FAVOURITE_MOVIE_ID = 7;
    public static final int COL_FAVOURITE_MOVIE_BACKDROP_PATH = 8;
    public static final int COL_FAVOURITE_MOVIE_POPULARITY = 9;
    public static final int COL_FAVOURITE_MOVIE_VOTE_COUNT = 10;
    public static final int COL_TRAILER_NAME = 1;
    public static final int COL_TRAILER_SIZE = 2;
    public static final int COL_TRAILER_SOURCE = 3;
    public static final int COL_TRAILER_TYPE_DATE = 4;
    public static final int COL_TRAILER_MOVIE_ID = 5;
    public static final int COL_REVIEW_PAGE = 1;
    public static final int COL_REVIEW_TOTAL_PAGE = 2;
    public static final int COL_REVIEW_TOTAL_RESULTS = 3;
    public static final int COL_REVIEW_IDS = 4;
    public static final int COL_REVIEW_AUTHOR = 5;
    public static final int COL_REVIEW_CONTENT = 6;
    public static final int COL_REVIEW_URL = 7;
    public static final int COL_REVIEW_MOVIE_ID = 8;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerViews();
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = getArguments().getParcelable(Constants.MOVIE_DETAILS_URI);
            Log.d("URI", mUri.toString());
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIE_FAVOURITE)) {
                favourite = savedInstanceState.getBoolean(MOVIE_FAVOURITE);
                if (favourite) {
                    favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_fav));
                } else {
                    favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                }
            }
            if (savedInstanceState.containsKey(MOVIE_ID)) {
                movieId = savedInstanceState.getString(MOVIE_ID);
            }
        }
        movieValues = new ContentValues();
        favMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favourite) {
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE_KEY, "0");
                    getActivity().getContentResolver().update(MoviesContract.MoviesEntry.buildMovieUriWithId(movieId), movieValues, null, null);
                    favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                    getActivity().getContentResolver().delete(MoviesContract.FavouritesEntry.buildFavouritesUriWithId(movieId), null, null);
                    favourite = false;
                } else {
                    getActivity().getContentResolver().insert(MoviesContract.FavouritesEntry.buildFavouritesUri(), movieValues);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE_KEY, "1");
                    getActivity().getContentResolver().update(MoviesContract.MoviesEntry.buildMovieUriWithId(movieId), movieValues, null, null);
                    favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_fav));
                    favourite = true;
                }
                movieValues.remove(MoviesContract.MoviesEntry.COLUMN_FAVOURITE_KEY);
            }
        });
        return view;
    }

    public void setupRecyclerViews() {
        trailerLayoutManager = new LinearLayoutManager(getActivity());
        reviewLayoutManager = new LinearLayoutManager(getActivity());
        trailers.setLayoutManager(trailerLayoutManager);
        reviews.setLayoutManager(reviewLayoutManager);
        Uri trailerUri = MoviesContract.TrailersEntry.buildTrailersUriWithId(movieId);
        Uri reviewUri = MoviesContract.ReviewsEntry.buildReviewsUriWithMovieId(movieId);
        Cursor trailerCursor = getActivity().getContentResolver().query(trailerUri,
                TRAILER_COLUMNS, null, null, null);
        Cursor reviewCursor = getActivity().getContentResolver().query(reviewUri,
                REVIEW_COLUMNS, null, null, null);
        trailerMovieAdapter = new TrailerAdapter(getActivity(), trailerCursor);
        reviewMovieAdapter = new ReviewAdapter(getActivity(), reviewCursor);
        trailers.setAdapter(trailerMovieAdapter);
        reviews.setAdapter(reviewMovieAdapter);
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
        switch (id) {
            case LOADER_ID:
                if (mUri != null) {
                    Log.d("Loader", "Create");
                    return new CursorLoader(getActivity(),
                            mUri,
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                }
                break;
            case TRAILER_LOADER_ID:
                if (trailersUri != null) {
                    Log.d("Loader", "Create");
                    return new CursorLoader(getActivity(),
                            trailersUri,
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null);
                }
                break;
            case REVIEW_LOADER_ID:
                if (reviewsUri != null) {
                    Log.d("Loader", "Create");
                    return new CursorLoader(getActivity(),
                            reviewsUri,
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null);
                }
                break;

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (!data.moveToFirst()) {
            return;
        }

        if (data.getColumnCount() == 0) {

        }
        switch (loader.getId()) {
            case LOADER_ID:
                String imageBaseUrl = getActivity().getString(R.string.image_base_url);
                Log.d("Size", String.valueOf(data.getCount()));


                String posterValue = data.getString(COL_MOVIE_POSTER);
                String titleValue = data.getString(COL_MOVIE_TITLE);
                String ratingValue = data.getString(COL_MOVIE_RATING);
                String summaryValue = data.getString(COL_MOVIE_SUMMARY);
                String fav = data.getString(COL_MOVIE_FAVOURITE);
                String movieIdValue = data.getString(COL_MOVIE_ID);
                String releaseDateValue = data.getString(COL_MOVIE_RELEASE_DATE);
                String adultValue = data.getString(COL_MOVIE_ADULT);
                String backdropValue = data.getString(COL_MOVIE_BACKDROP_PATH);
                String popularityValue = data.getString(COL_MOVIE_POPULARITY);
                String voteCountValue = data.getString(COL_MOVIE_VOTE_COUNT);

                movieId = movieIdValue;
                try {
                    trailersUri = MoviesContract.TrailersEntry.buildTrailersUriWithId(movieIdValue);
                    reviewsUri = MoviesContract.ReviewsEntry.buildReviewsUriWithMovieId(movieIdValue);
                    FetchTrailerReviews();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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

                if (!releaseDateValue.equals("")) {
                    String[] dateParameters;
//            dateParameters = releaseDateValue.split("-");
//
//
//            //Creating a new instance of calendar
//            Calendar calendar = Calendar.getInstance();
//            //Setting the calendar time to the one received as releaseDate
//            if (dateParameters.length > 0) {
//                calendar.set(Integer.valueOf(dateParameters[0]), Integer.valueOf(dateParameters[1]), Integer.valueOf(dateParameters[2]));
//                Date date = new Date(calendar.getTimeInMillis());
//                //Used to format the date as required
//                SimpleDateFormat sdf = new SimpleDateFormat("dd mm yyyy", Locale.US);
//                releaseDateValue = sdf.format(date);
                    releaseDate.setText("Release Date : \n" + releaseDateValue);

                }
                //Set the values for each view

                rating.setText("\nRating : " + ratingValue + "/10");
                title.setText(titleValue);
                summary.setText(summaryValue);

                if (fav != null) {
                    if (fav.equals("1")) {
                        favourite = true;
                        favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_fav));
                    } else {
                        favourite = false;
                        favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                    }
                } else {
                    favourite = false;
                    favMovie.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border));
                }

                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_TITLE_KEY, titleValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_POSTER_KEY, posterValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_SUMMARY_KEY, summaryValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_RELEASE_DATE_KEY, releaseDateValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_RATING_KEY, ratingValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_ADULT_KEY, adultValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID_KEY, movieIdValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_BACKDROP_PATH_KEY, backdropValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_POPULARITY_KEY, popularityValue);
                movieValues.put(MoviesContract.FavouritesEntry.COLUMN_VOTE_COUNT_KEY, voteCountValue);
                break;
            case TRAILER_LOADER_ID:
                Log.d("loader", "here");
                trailerText.setVisibility(View.VISIBLE);
                trailerMovieAdapter.swapCursor(data);
                break;
            case REVIEW_LOADER_ID:
                reviewText.setVisibility(View.VISIBLE);
                reviewMovieAdapter.swapCursor(data);
                break;

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MOVIE_FAVOURITE, favourite);
        outState.putString(MOVIE_ID, movieId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public void FetchTrailerReviews() throws MalformedURLException {

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
                            Log.d("response", response.toString());
                            getTrailersReviews(response);

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

    /**
     * Function to return url to be called to fetch the movies
     *
     * @return
     * @throws MalformedURLException
     */
    public String setUrl() throws MalformedURLException {

        final String MOVIES_BASE_URL = getString(R.string.movie_by_id_base_url);
        final String API_PARAM = getString(R.string.api_param_key);
        final String APPEND_DATA = getString(R.string.append_response);
        final String api_key = getActivity().getApplicationContext().getResources().
                getString(R.string.api_key);


        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(API_PARAM, api_key)
                .appendQueryParameter(APPEND_DATA, getString(R.string.append_parameter_value))
                .build();
        URL url = new URL(builtUri.toString());
        return url.toString();
    }

    public void getTrailersReviews(JSONObject response) {
        // These are the names of the JSON objects that need to be extracted.
        final String NAME = "name";
        final String SIZE = "size";
        final String SOURCE = "source";
        final String TYPE = "type";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";
        final String TOTAL_PAGES_REVIEWS = "total_pages";
        final String TOTAL_RESULTS_REVIEWS = "total_results";
        final String PAGE = "page";
        final String MOVIE_ID = "id";
        final String TRAILERS = "trailers";
        final String REVIEWS = "reviews";
        final String YOUTUBE = "youtube";


        try {

            JSONArray movieArrayTrailer = response.getJSONObject(TRAILERS).getJSONArray(YOUTUBE);
            JSONObject movieArrayReviews = response.getJSONObject(REVIEWS);
            String movieId = response.getString(MOVIE_ID);

            Vector<ContentValues> cVVectorTrailer = new Vector<ContentValues>(movieArrayTrailer.length());

            for (int i = 0; i < movieArrayTrailer.length(); i++) {

                JSONObject trailerInfo = movieArrayTrailer.getJSONObject(i);


                String name;
                String size;
                String source;
                String type;
                name = trailerInfo.getString(NAME);
                size = trailerInfo.getString(SIZE);
                source = trailerInfo.getString(SOURCE);
                type = trailerInfo.getString(TYPE);

                ContentValues movieValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.

                movieValues.put(MoviesContract.TrailersEntry.COLUMN_NAME_KEY, name);
                movieValues.put(MoviesContract.TrailersEntry.COLUMN_SIZE_KEY, size);
                movieValues.put(MoviesContract.TrailersEntry.COLUMN_SOURCE_KEY, source);
                movieValues.put(MoviesContract.TrailersEntry.COLUMN_TYPE_DATE_KEY, type);
                movieValues.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_ID_KEY, movieId);
                cVVectorTrailer.add(movieValues);

            }
            int insert = 0;
            // add to database
            if (cVVectorTrailer.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorTrailer.size()];
                cVVectorTrailer.toArray(cvArray);
                insert = getActivity().getContentResolver().bulkInsert(MoviesContract.TrailersEntry.CONTENT_URI, cvArray);
                getLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);

            } else {
                trailerText.setVisibility(View.VISIBLE);
                trailerText.setText("No trailer Available");
            }
            //Log.d(LOG_TAG, "FetchTrailer Task Complete. " + inserted + " Inserted");


            String page = movieArrayReviews.getString(PAGE);
            String totalPage = movieArrayReviews.getString(TOTAL_PAGES_REVIEWS);
            String totalResults = movieArrayReviews.getString(TOTAL_RESULTS_REVIEWS);

            JSONArray reviews = movieArrayReviews.getJSONArray("results");

            Vector<ContentValues> cVVectorReviews = new Vector<ContentValues>(reviews.length());

            for (int j = 0; j < reviews.length(); j++) {

                JSONObject reviewsInfo = reviews.getJSONObject(j);

                String idReviews;
                String author;
                String content;
                String url;


                idReviews = reviewsInfo.getString(REVIEW_ID);
                author = reviewsInfo.getString(AUTHOR);
                content = reviewsInfo.getString(CONTENT);
                url = reviewsInfo.getString(URL);

                ContentValues movieValues = new ContentValues();

                // Then add the data, along with the corresponding name of the data type,
                // so the content provider knows what kind of value is being inserted.

                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_PAGE_KEY, page);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_TOTAL_PAGE_KEY, totalPage);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_TOTAL_RESULTS_KEY, totalResults);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_ID_REVIEWS_KEY, idReviews);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_AUTHOR_KEY, author);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_CONTENT_KEY, content);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_URL_KEY, url);
                movieValues.put(MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID_KEY, movieId);

                cVVectorReviews.add(movieValues);

            }
            insert = 0;
            if (cVVectorReviews.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorReviews.size()];
                cVVectorReviews.toArray(cvArray);
                insert = getActivity().getContentResolver().bulkInsert(MoviesContract.ReviewsEntry.CONTENT_URI, cvArray);
                getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
            } else {
                reviewText.setVisibility(View.VISIBLE);
                reviewText.setText("No Reviews Available");
            }

        } catch (JSONException e) {
            //Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    /**
     * Custom adapter to load images in the recyclerView
     */
    public class ReviewAdapter extends CursorRecyclerViewAdapter<ReviewAdapter.ViewHolder> {


        Context mContext;
        //private ArrayList<Movie> mDataset;
        private Cursor mCursor;

        /**
         * Provide a reference to the views for each data item
         * Complex data items may need more than one view per item, and
         * we provide access to all the views for a data item in a view holder
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.review_author)
            TextView author;
            @Bind(R.id.review_content)
            TextView contentView;
            @Bind(R.id.review_url)
            TextView urlView;


            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public ReviewAdapter(Context mContext, Cursor cursor) {
            //mDataset = moviesDataset;
            super(mContext, cursor);
            this.mContext = mContext;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_movie, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

            cursor.moveToFirst();
            final String authorName = cursor.getString(MovieDetailFragment.COL_REVIEW_AUTHOR);
            final String content = cursor.getString(MovieDetailFragment.COL_REVIEW_CONTENT);
            final String url = cursor.getString(MovieDetailFragment.COL_REVIEW_URL);
            holder.author.setText("Author:  " + authorName);
            holder.contentView.setText(content);
            holder.urlView.setText("For more:  " + url);

        }

    }

    /**
     * Custom adapter to load images in the recyclerView
     */
    public class TrailerAdapter extends CursorRecyclerViewAdapter<TrailerAdapter.ViewHolder> {


        Context mContext;
        private final View.OnClickListener mOnClickListener = new RecyclerViewOnClickListener();
        //private ArrayList<Movie> mDataset;
        private Cursor mCursor;
        String trailerUrl;

        /**
         * Provide a reference to the views for each data item
         * Complex data items may need more than one view per item, and
         * we provide access to all the views for a data item in a view holder
         */

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @Bind(R.id.title)
            TextView title;
            @Bind(R.id.thumbnail)
            ImageView thumbnail;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public TrailerAdapter(Context mContext, Cursor cursor) {
            //mDataset = moviesDataset;
            super(mContext, cursor);
            this.mContext = mContext;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trailer_movie, parent, false);
            view.setOnClickListener(mOnClickListener);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
            cursor.moveToFirst();
            final String trailerName = cursor.getString(MovieDetailFragment.COL_TRAILER_NAME);
            final String source = cursor.getString(MovieDetailFragment.COL_TRAILER_SOURCE);
            holder.title.setText(trailerName);
            final String BASE_URL = mContext.getString(R.string.youtube_thumbnail_base_url);
            final String url = BASE_URL + source + "/0.jpg";
            Picasso
                    .with(mContext)
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.thumbnail);
            holder.thumbnail.setAdjustViewBounds(true);

            trailerUrl = mContext.getString(R.string.youtube_base_url) + source;


        }

        public class RecyclerViewOnClickListener implements View.OnClickListener {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                mContext.startActivity(intent);
            }
        }

    }
}

