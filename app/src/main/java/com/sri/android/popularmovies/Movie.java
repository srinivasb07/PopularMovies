package com.sri.android.popularmovies;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Movie Model
 */
public class Movie {
    long id;
    String posterPath;
    String overView;
    boolean adult;
    String release_date;
    String originalTitle;
    String original_lang;
    String title;
    String backdropPath;
    double popularity;
    List<Long> genreIds;
    long vote_count;
    double vote_avg;

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    final String BKG_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public Movie(boolean adult, String backdropPath, List<Long> genreIds, long id, String original_lang, String originalTitle, String overview, String releaseDate, String posterPath, double popularity, String title, double voteAverage, long voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.genreIds = genreIds;
        this.id = id;
        this.original_lang = original_lang;
        this.originalTitle = originalTitle;
        this.overView = overview;
        this.release_date = releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.title = title;
        this.vote_avg = voteAverage;
        this.vote_count = voteCount;
    }


    public Movie(JSONObject movie) throws JSONException {
        this.id = movie.getInt("id");
        this.original_lang = movie.getString("original_language");
        this.originalTitle = movie.getString("original_title");
        this.overView = movie.getString("overview");
        this.release_date = movie.getString("release_date");

        this.posterPath = Uri.parse(POSTER_BASE_URL).buildUpon().
                appendEncodedPath(movie.getString("poster_path")).build().toString();
        this.backdropPath = Uri.parse(BKG_BASE_URL).buildUpon().
                appendEncodedPath(movie.getString("backdrop_path")).build().toString();

        this.popularity = movie.getDouble("popularity");
        this.vote_avg = movie.getDouble("vote_average");
    }
}
