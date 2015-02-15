package com.movie.watch;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.movie.watch.MovieMeter.MovieListAdapter;

public class GetMovieInfoTask extends AsyncTask<String, Void, ArrayList<Movie>>{
	
	private String urlString;
	private LinearLayout progress;
	private MovieListAdapter mAdapter;

	public GetMovieInfoTask(LinearLayout p, MovieListAdapter m){
		progress = p;
		mAdapter = m;
	}
	
	@Override
    protected void onPreExecute()
    {
    	progress.setVisibility(View.VISIBLE);
    }
    	
    @Override
    protected ArrayList<Movie> doInBackground(String... params) 
    {
    	urlString = params[0];  	
    	return new OpenSocket(params[0]).setMovies();
    }
    
    @Override
    protected void onPostExecute(ArrayList<Movie> moviesList)
    {
    	progress.setVisibility(View.GONE);
    	if(moviesList != null && moviesList.size() > 0){
    		mAdapter.setList(moviesList);	
    		if(urlString.contains(MovieMeter.BOX_OFFICE)){
    			MovieMeter.boxOffice.clear();
    			MovieMeter.boxOffice.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.IN_THEATRES)){
    			MovieMeter.inTheatres.clear();
    			MovieMeter.inTheatres.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.OPENING)){
    			MovieMeter.opening.clear();
    			MovieMeter.opening.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.UPCOMING_MOVIES)){
    			MovieMeter.comingSoon.clear();
    			MovieMeter.comingSoon.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.TOP_RENTALS)){
    			MovieMeter.rentals.clear();
    			MovieMeter.rentals.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.CURRENT_RELEASES)){
    			MovieMeter.currentReleases.clear();
    			MovieMeter.currentReleases.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.NEW_RELEASES)){
    			MovieMeter.newReleases.clear();
    			MovieMeter.newReleases.addAll(moviesList);
    		}
    		else if(urlString.contains(MovieMeter.UPCOMING_DVDS)){
    			MovieMeter.releasedSoon.clear();
    			MovieMeter.releasedSoon.addAll(moviesList);
    		}
    	}
    }   
}
