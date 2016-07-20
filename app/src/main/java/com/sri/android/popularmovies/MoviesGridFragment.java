package com.sri.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesGridFragment extends Fragment {

    ArrayAdapter<Movie> mAdapter;
    private final String LOG_TAG = MoviesGridFragment.class.getSimpleName();
    private String lastSortingOrder="initial";

    public MoviesGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mAdapter = new PosterArrayAdapter(getActivity(),R.layout.movie_item,new ArrayList<Movie>());
        final GridView mGrid = (GridView)rootView.findViewById(R.id.grid_movies);
        mGrid.setAdapter(mAdapter);
        mGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int size = mGrid.getWidth();
                int numCol = (int) Math.round((double) size / (double) getResources().getDimensionPixelSize(R.dimen.poster_width));
                mGrid.setNumColumns(numCol);
            }
        });
        mGrid.setNumColumns(5);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie movieInfo = (Movie) adapterView.getItemAtPosition(position);

                long MOVIEID = movieInfo.id;
                String ORGLANG = movieInfo.original_lang;
                String ORGTITLE = movieInfo.originalTitle;
                String OVER = movieInfo.overView;
                String RELDATE = movieInfo.release_date;
                String POSTURL = movieInfo.posterPath;
                double POPULARITY = movieInfo.popularity;
                double VOTAVG = movieInfo.vote_avg;
                String BKGURL = movieInfo.backdropPath;

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("movieId", MOVIEID)
                        .putExtra("orgLang", ORGLANG)
                        .putExtra("orgTitle", ORGTITLE)
                        .putExtra("overview", OVER)
                        .putExtra("relDate", RELDATE)
                        .putExtra("postUrl", POSTURL)
                        .putExtra("popularity", POPULARITY)
                        .putExtra("voteAvg", VOTAVG)
                        .putExtra("bkgURL", BKGURL);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingOrder = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_def_sort));
        if(!sortingOrder.equals(lastSortingOrder)) {
            mAdapter.clear();
            lastSortingOrder = sortingOrder;
        }

        if(isNetworkAvailable()){
            updateMovieList();
        }else{
            Log.e(LOG_TAG, "Network not reachable");
        }
    }

    private void updateMovieList() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this);
        moviesTask.execute(lastSortingOrder);
    }
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
