package com.movie.watch;

import java.util.ArrayList;

import android.os.AsyncTask;

public class GetAlternativeTitlesTask extends AsyncTask<String, Void, ArrayList<String>>{
    	
	private Movie movie;
	
	//constructors
	public GetAlternativeTitlesTask(){}
	public GetAlternativeTitlesTask(Movie m){
		movie = m;
	}
	
    @Override
    protected ArrayList<String> doInBackground(String... params) 
    {
    	return new OpenSocket(params[0]).getTMDBTitles(movie.getImdbId());
    }
    
    @Override
    protected void onPostExecute(ArrayList<String> titles)
    {
    	if(titles != null && titles.size() > 0){
    		movie.setAlternativeTitles(titles);
    	}
    }   
}
