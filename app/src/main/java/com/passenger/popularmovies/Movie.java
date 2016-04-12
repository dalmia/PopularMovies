package com.passenger.popularmovies;

/**
 * Custom object for each movie that we fetch
 */
public class Movie {

    String title;
    String poster;
    String summary;
    String releaseDate;
    String rating;

    public Movie(String title, String poster, String summary, String releaseDate, String rating){
        this.title = title;
        this.poster = poster;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.rating = rating;

    }


}
