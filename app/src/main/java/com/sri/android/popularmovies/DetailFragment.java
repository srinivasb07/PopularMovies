package com.sri.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {
    String orgLang;
    String orgTitle;
    String overview;
    String relDate;
    String postURL;
    double popularity;
    double votAvg;
    String bkgURL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("movieId")){
            orgTitle=intent.getStringExtra("orgTitle");
            ((TextView) rootView.findViewById(R.id.orgtitle))
                    .setText("Original Title :   "+orgTitle);

            orgLang=intent.getStringExtra("orgLang");
            ((TextView) rootView.findViewById(R.id.lang))
                    .setText("Original Language :  "+orgLang);

            overview=intent.getStringExtra("overview");
            ((TextView) rootView.findViewById(R.id.overview))
                    .setText(overview);

            relDate=intent.getStringExtra("relDate");
            ((TextView) rootView.findViewById(R.id.Reldate))
                    .setText("Release Date :    " +relDate);


            bkgURL=intent.getStringExtra("bkgURL");
            ImageView bkgposter = (ImageView) rootView.findViewById(R.id.bkgposter);
            Picasso
                    .with(getActivity())
                    .load(bkgURL)
                    .fit()
                    .into(bkgposter);

            postURL=intent.getStringExtra("postUrl");
            ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
            Picasso
                    .with(getActivity())
                    .load(postURL)
                    .fit()
                    .into(poster);

            popularity=intent.getDoubleExtra("popularity", 0);
            ((TextView) rootView.findViewById(R.id.popu))
                    .setText("Popularity :  "+popularity);

            votAvg=intent.getDoubleExtra("voteAvg", 0);
            ((TextView) rootView.findViewById(R.id.avgVote))
                    .setText("User Rating :     "+votAvg+"/10");

        }

        return rootView;
    }
}
