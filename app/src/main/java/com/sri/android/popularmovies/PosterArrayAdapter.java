package com.sri.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PosterArrayAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private int mResource;
    private ArrayList<Movie> mMovieInfoList;

    public PosterArrayAdapter(Activity context, int resource, ArrayList<Movie> movieInfoList) {
        super(context, R.layout.movie_item, movieInfoList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        String url = movie.posterPath;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);

        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_poster_item);

        Picasso.with(mContext)
                .load(url)
                .error(R.mipmap.ic_launcher)
                .fit()
                .into(imageView);

        imageView.setAdjustViewBounds(true);
        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
