package com.movie.watch;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowtimesFragment extends Fragment{
	private final String MOVIE_TIMES_URL = "http://www.google.com/movies?near=";
	
	private Movie movie;
	private String url;
	private String [] urls;
	private boolean isShowtimesChecked = false;
	
	private ArrayList<Showtimes> showtimesList = new ArrayList<Showtimes>();
	private ShowtimesAdapter tAdapter;
	
	//layout
	private LinearLayout showtimesProgress;
	private TextView noShowtimes;
	
	//Colors
	int darkGrey;

	
	static ShowtimesFragment newInstance(Movie movie){
		ShowtimesFragment s = new ShowtimesFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("Movie", movie);
		s.setArguments(args);
		
		return s;
	}
	
	@Override
	public void onAttach(Activity a){
		super.onAttach(a);
		tAdapter = new ShowtimesAdapter(a, R.layout.showtimes_layout, showtimesList);
		darkGrey = getResources().getColor(R.color.DarkTransparentBlack);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			//movie
			movie = getArguments().getParcelable("Movie");
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//Inflate the layout for this fragment
		View showtimesView = inflater.inflate(R.layout.showtimes_list_layout, null);

		showtimesProgress = (LinearLayout) showtimesView.findViewById(R.id.showtimes_progress_layout);
		noShowtimes = (TextView) showtimesView.findViewById(R.id.no_showtimes);
		
		ListView showtimesListView = (ListView) showtimesView.findViewById(R.id.showtimes_list); 
        showtimesListView.setAdapter(tAdapter);
        
        showtimesProgress.setVisibility(View.GONE);
        noShowtimes.setVisibility(View.GONE);
        
        //url
		if(DisplayMovie.locationCoordinates != null){
			url = MOVIE_TIMES_URL + DisplayMovie.locationCoordinates + "&hl=en" + "&q=" + movie.getTitle();
    		//url = MOVIE_TIMES_URL + "50.1862,8.665078&hl=en&q=" + movie.getTitle();
    		//Log.d(MovieMeter.LOCATION_STRING, "Showtimes URL: " + url);
    	}
    	else{ //returns empty string -- does nothing
    		url = MOVIE_TIMES_URL + "&hl=en" + "&q=" + movie.getTitle();
    	}
		//Log.d(MovieMeter.LOCATION_STRING, "Location in Showtimes: " + DisplayMovie.locationCoordinates);
        
        return showtimesView;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //check if there are alternative titles available and create array
        if(movie.getAlternativeTitles() != null){
        	urls = new String[movie.getAlternativeTitles().size()];
        	for(int i=0; i<urls.length; i++){
        		if(DisplayMovie.locationCoordinates != null){
        			urls[i] = MOVIE_TIMES_URL + DisplayMovie.locationCoordinates + "&hl=en&q=" + movie.getAlternativeTitles().get(i); 
        			//urls[i] = MOVIE_TIMES_URL + "50.1862,8.665078&hl=en&q=" + movie.getAlternativeTitles().get(i);
        		}
        		else{
        			urls[i] = MOVIE_TIMES_URL + "&hl=en" + "&q=" + movie.getAlternativeTitles().get(i);
        		}
        	}
        }
        
        if(showtimesList.isEmpty() || showtimesList == null){
        	if(isShowtimesChecked == false){
        		//start thread for showtimes
        		new GetShowtimesTask().execute(url);
        	}
        	else{
        		noShowtimes.setVisibility(View.VISIBLE);
        	}
        }
	}

	
	class ShowtimesAdapter extends ArrayAdapter<Showtimes>{
    	
		public ShowtimesAdapter(Context context, int textViewResourceId, ArrayList<Showtimes> items)
		{
			super(context, textViewResourceId, items);
			showtimesList = items;
		}
		
		public void setList(ArrayList<Showtimes> items) {
			showtimesList.clear(); 
			showtimesList.addAll(items);
	        this.notifyDataSetChanged();
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if(v == null) 
			{  
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                v = vi.inflate(R.layout.showtimes_layout, null);
			}  

			Showtimes showtime = showtimesList.get(position);
			
			TextView movieTitle = (TextView) v.findViewById(R.id.movie_title);
			
			//RelativeLayout cinemaTimesLayout = (RelativeLayout) v.findViewById(R.id.cinema_layout);
			TextView theatreName = (TextView) v.findViewById(R.id.cinema_name);
			TextView times = (TextView) v.findViewById(R.id.times);
			
			movieTitle.setVisibility(View.GONE);
			
			if(showtimesList.size() > 0){
				//Log.d("Showtime", "Size of Showtimes Array = " + shows.size());
				//Showtimes 
				try{
					String title = showtime.getMovieTitle();
					if(title != null & title.length() > 0){
						movieTitle.setText(title);
						movieTitle.setVisibility(View.VISIBLE);
					}
					else{
						movieTitle.setVisibility(View.GONE);
					}
				}
				catch(NullPointerException e){ 
					Log.e("Showtime", "Caught NullPointerException for null value in Movie Title");
				}
				
		        String name = showtime.getTheatre().getName();
		        //Log.d("Showtime", "Name of Theatre = " + name);
		        //ArrayList<String> timesList = showtime.getTimes();
		        //Log.d("Showtime", "tempTitle = " + tempTitle);

		        theatreName.setText(name);
		        times.setText(showtime.displayTimes());
		        /*if(position % 2 == 0) //even number
		        {
		        	cinemaTimesLayout.setBackgroundColor(darkGrey);
		        	//times.setBackgroundColor(darkGrey);
		        }*/

	        }

	        return v;
		}     	
    }
	
	private ArrayList<Showtimes> getShowtimes(int iterator, String... s){
		ArrayList<Showtimes> shows = new ArrayList<Showtimes>();
    	String sURL = "";
		for(int i=0; i<iterator; i++){
			try{
				sURL = s[i];
				URL url = new URL(sURL);
		    	URI uri = new URI(url.getProtocol(), url.getAuthority(), url.getPath(), url.getQuery(), null);
		    	sURL = uri.toString();
		    	Log.i("Showtime", "Modified URi = " + sURL);
	    		
		    	OpenSocket os = new OpenSocket(sURL);
		    	//movie.setClipLink(os.getTrailerURL(movieTitle));
		    	//Log.i("Showtime", "Movie Title = " + movie.getTitle());
		    	shows = os.getShowtimes();
		    	if(!shows.isEmpty()){
		    		return shows;
		    	}
			}
			catch(Exception e){
				Log.e("Showtime", "Malformed URL Expression for URL: " + sURL + " " + e.toString());
			}
		}
		return null;
	}
	
	private class GetShowtimesTask extends AsyncTask<String, Void, ArrayList<Showtimes>>{
		
		@Override
	    protected void onPreExecute()
	    {
			showtimesProgress.setVisibility(View.VISIBLE);
			noShowtimes.setVisibility(View.GONE);
	    }
	    	
	    @Override
	    protected ArrayList<Showtimes> doInBackground(String... params) 
	    {
	    	ArrayList<Showtimes> showtimes = new ArrayList<Showtimes>();
	    	String url = params[0];

    		showtimes = getShowtimes(params.length, url);
    		if(showtimes == null && urls != null){
	    		showtimes = getShowtimes(urls.length, urls);
    		}

	    	return showtimes;//os.getShowtimes(movie.getTitle());
	    }
	    
	    @Override
	    protected void onPostExecute(ArrayList<Showtimes> showtimes)
	    {
	    	//check we have a trailer link from google
	    	//if(movie.getClipLink() != null && movie.getClipLink().length() > 1){
	    		//vPlay.setVisibility(View.VISIBLE);
	    		//Log.i("Showtime", "Movie Clip Link = " + movie.getClipLink());
	    	//}
	    	//if not go get the link from rotten tomoatoes
	    	//else
	    		//new GetTrailerTask().execute(MOVIE_URL_BASE + movieId + CLIPS_URL_BASE); 
	    	
	    	//now populate showtimes adapter
	    	if(showtimes != null && !showtimes.isEmpty()){
	    		Log.i("Showtime", "Showtimes Size = " + showtimes.size());
	    		//do stuff
	    		//movie.setShowtimes(showtimes);
	    		tAdapter.setList(showtimes);
	    		Log.i("Showtime", "Showtimes Size = " + showtimes.size());
	    	}
	    	else{
	    		noShowtimes.setVisibility(View.VISIBLE);
	    	}
	    	
	    	showtimesProgress.setVisibility(View.GONE);
	    	isShowtimesChecked = true;
	    }    
    }
}
