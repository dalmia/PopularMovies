package com.passenger.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.passenger.popularmovies.Constants.MOVIE_DETAILS;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {


    ImageView poster;
    TextView title, summary, releaseDate, rating;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initiate(view);
        if (getActivity().getIntent() != null) {
            Bundle movieDetails = getActivity().getIntent().getExtras();
            Movie movie = new Gson().fromJson(movieDetails.getSerializable(MOVIE_DETAILS).toString(),
                    new TypeToken<Movie>() {
                    }.getType());
            String imageBaseUrl = getActivity().getString(R.string.image_base_url);
            if (movie.poster != null) {
                Picasso.with(getActivity())
                        .load(imageBaseUrl + movie.poster)
                        .into(poster);
            }
            String[] dateParameters = movie.releaseDate.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(dateParameters[0]),Integer.valueOf(dateParameters[1]),Integer.valueOf(dateParameters[2]));
            Date date = new Date(calendar.getTimeInMillis());
            rating.setText("\nRating : " + movie.rating + "/10" );
            title.setText(movie.title);
            summary.setText(movie.summary);
            SimpleDateFormat sdf = new SimpleDateFormat("dd EEEE yyyy", Locale.US);
            movie.releaseDate = sdf.format(date);
            releaseDate.setText("Release Date : \n" + movie.releaseDate);


        }
        return view;

    }

    public void initiate(View view) {
        poster = (ImageView) view.findViewById(R.id.poster);
        title = (TextView) view.findViewById(R.id.title);
        summary = (TextView) view.findViewById(R.id.summary);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        rating = (TextView) view.findViewById(R.id.rating);
    }


}
