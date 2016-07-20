package com.sri.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>>{

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MoviesGridFragment parentFragment;

    FetchMoviesTask(){
        super();
    }
    FetchMoviesTask(MoviesGridFragment parent){
        this.parentFragment = parent;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if(params.length == 0)
            return null;

        Log.d(LOG_TAG,"Sort By: "+params[0]);
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJSONStr = null;

        try{
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String APP_ID_PARAM = "api_key";
            final String PAGE_PARAM = "page";
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(APP_ID_PARAM, BuildConfig.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                //Adding newline for easy debugging
                buffer.append(line + "\n");
            }

            // Check if buffer is empty
            if (buffer.length() == 0) {
                return null;
            }
            moviesJSONStr = buffer.toString();
        }
        catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
           // Log.e(LOG_TAG, "Something happened... Showing the dummy/old data now");
          //  moviesJSONStr = "{         \"page\": 1,         \"results\": [ {     \"adult\": false,         \"backdrop_path\": \"/dkMD5qlogeRMiEixC4YNPUvax2T.jpg\",         \"genre_ids\": [     28,             12,             878,             53     ],     \"id\": 135397,         \"original_language\": \"en\",         \"original_title\": \"Jurassic World\",         \"overview\": \"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.\",         \"release_date\": \"2015-06-12\",         \"poster_path\": \"/uXZYawqUsChGSj54wcuBtEdUJbh.jpg\",         \"popularity\": 88.551849,         \"title\": \"Jurassic World\",         \"video\": false,         \"vote_average\": 7.1,         \"vote_count\": 435 }, {     \"adult\": false,         \"backdrop_path\": \"/jxPeRkfOoWs6gFybOa8C4xrHLrm.jpg\",         \"genre_ids\": [     53,             28,             12     ],     \"id\": 76341,         \"original_language\": \"en\",         \"original_title\": \"Mad Max: Fury Road\",         \"overview\": \"An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.\",         \"release_date\": \"2015-05-15\",         \"poster_path\": \"/kqjL17yufvn9OVLyXYpvtyrFfak.jpg\",         \"popularity\": 35.88189,         \"title\": \"Mad Max: Fury Road\",         \"video\": false,         \"vote_average\": 7.9,         \"vote_count\": 815 }]}";
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJSONStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }


    public ArrayList getMoviesDataFromJson(String jsonStr) throws JSONException{

        final String RESULT = "results";

        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray(RESULT);

        ArrayList data = new ArrayList();

        for(int i=0;i<movieArray.length();i++){
            JSONObject movie = movieArray.getJSONObject(i);
            data.add(new Movie(movie));
        }
        return data;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {

        if (result != null) {
            for(Movie movie : result) {
                this.parentFragment.mAdapter.add(movie);
            }
            //PAGE_LOADED++;
        }
        //stopLoad();
    }
}
