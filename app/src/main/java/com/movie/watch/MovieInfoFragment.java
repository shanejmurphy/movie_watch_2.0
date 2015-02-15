package com.movie.watch;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieInfoFragment extends Fragment{
	
	//TMDB
	//api key -- append to above urls
	public static final String TMDB_API_KEY = "?api_key=b452ceeb800e4bc73b22bbbb83635f87";
	
	//tmdb access
	public static final String TMDB_BASE_URL = "http://api.themoviedb.org/";
	public static final String TMDB_AUTH_URL = "3/authentication/token/new";
	public static final String TMDB_SESSION_URL = "3/authentication/session/new";
	
	//guest session
	public static final String TMDB_GUEST_URL = TMDB_BASE_URL + "3/authentication/guest_session/new" + TMDB_API_KEY;
	
	//rotten tomatoes
	public static final String CLIPS_URL_BASE = "/clips.json?" + MovieMeter.API_KEY.substring(1, MovieMeter.API_KEY.length()); //no '&' required
	
	private Movie movie;
	private Bitmap poster;
	
	//layout vars
	private TextView vReleaseDate;
	//button for trailers
	private ImageButton vPlay;
	
	private boolean isAlternativeTitlesChecked = false;
	
	
	/**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static MovieInfoFragment newInstance(Movie movie, Bitmap poster) {
    	MovieInfoFragment m = new MovieInfoFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("Movie", movie);
        args.putParcelable("Poster", poster);
        m.setArguments(args);

        return m;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	if(getArguments() != null){
			movie = getArguments().getParcelable("Movie");
			poster = getArguments().getParcelable("Poster");
		} 
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//drawables for the score
        Drawable fresh = getResources().getDrawable(R.drawable.fresh_small);
        Drawable rotten = getResources().getDrawable(R.drawable.rotten_small);
        Drawable good = getResources().getDrawable(R.drawable.popcorn_small);
        Drawable bad = getResources().getDrawable(R.drawable.bad_popcorn_small);
		
		// Inflate the layout for this fragment
		View info = inflater.inflate(R.layout.movie_info_layout, null);
		
		//UI content of header file
        LinearLayout rtLink = (LinearLayout) info.findViewById(R.id.text_beside_display_image);
        TextView vCriticScore = (TextView) info.findViewById(R.id.display_critic_rating);
        TextView vAudienceScore = (TextView) info.findViewById(R.id.display_audience_rating);
        //TextView vGenre = (TextView) header.findViewById(R.id.display_genre);
        vReleaseDate = (TextView) info.findViewById(R.id.display_release_date);
        TextView vRunningTime = (TextView) info.findViewById(R.id.display_running_time);
        TextView vRated = (TextView) info.findViewById(R.id.display_rated);
        TextView vCast = (TextView) info.findViewById(R.id.display_cast);
        TextView cSynopsis = (TextView) info.findViewById(R.id.display_synopsis);
        ImageView vPoster = (ImageView) info.findViewById(R.id.display_image);
        vPlay = (ImageButton) info.findViewById(R.id.play_button); 
        
        //must make sure play button is invisible when page first loads
        vPlay.setVisibility(View.GONE);
        
        //link to trailers page for movie
    	vPlay.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view)
			{
				//open in url for trailer
				final Uri uri;
				String link = movie.getClipLink();
				if(link != null){
					if(link.contains("rotten")){
						uri = Uri.parse(movie.getClipLink());
					}
					else{
						uri = Uri.parse("http://www.youtube.com/watch?v=" + movie.getClipLink());
					}
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				}
			}
		});
        
        //link to rotten tomatoes home page for movie -- required by terms of license
        rtLink.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view)
			{
				//open in url for trailer
				final Uri uri = Uri.parse(movie.getLink()); 
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
              
        //now get on with filling in page details
        vPoster.setImageBitmap(poster);
        
     	//check scores to enable right image for the movie
        //critics
        int cScore = movie.getCriticScore();

        //audience
        int aScore = movie.getAudienceScore();
        if(aScore > 59)
        { 
        	vAudienceScore.setCompoundDrawablesWithIntrinsicBounds(good, null, null, null);
        }
        else
        {
        	vAudienceScore.setCompoundDrawablesWithIntrinsicBounds(bad, null, null, null);
        }

        String criticScore = Integer.toString(cScore) + "% of critics liked it"; //convert integer to a string for display purposed
        String audienceScore = Integer.toString(aScore) + "% of the audience liked it"; //convert integer to a string for display purposed

        //check for no ratings
        if(criticScore.contains("-1%"))
        {
        	criticScore = "No Critic Reviews";
        	vCriticScore.setText(criticScore);
        	vCriticScore.setCompoundDrawablesWithIntrinsicBounds(rotten, null, null, null);

        }
        else if(criticScore.contains("100%"))
        {
        	vCriticScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
        	vCriticScore.setText(criticScore);
        }
        else if(cScore > 59)
        {
        	vCriticScore.setText(criticScore);     	
        	vCriticScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
        }
        else
        {
        	vCriticScore.setText(criticScore);
        	vCriticScore.setCompoundDrawablesWithIntrinsicBounds(rotten, null, null, null);
        }
        
        vAudienceScore.setText(audienceScore);

        //vGenre.setText(movie.getGenre);
        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null && releaseDate.length() > 1){
        	vReleaseDate.setText(movie.getLongReleaseDate());
        }
        else
        	vReleaseDate.setText("Unknown");
        
        
        vRunningTime.setText(movie.getLongRuntime());
        vRated.setText(movie.getRating());
        
        List<String> cast = movie.getCast();
        if(cast.size() > 0){
        	vCast.setText(movie.castToString(cast));
        }
        else
        	vCast.setText("Unknown");
        
        String syn = movie.getSynopsis();
        if(syn.length() > 1){
        	cSynopsis.setText(movie.getSynopsis());   
        }
        else
        	cSynopsis.setText("Not Available for this Movie"); 
        
        //alternative titles for showtimes lookup
        if((movie.getAlternativeTitles() == null) && (isAlternativeTitlesChecked == false)){
        	new GetAlternativeTitlesTask(movie).execute(TMDB_BASE_URL + "3/movie/" + movie.getFullImdbId() + "/alternative_titles" + TMDB_API_KEY);
        	isAlternativeTitlesChecked = true;
        }
        
        //play button 
    	if(movie.getClipLink() != null && movie.getClipLink().length() > 1){
    		vPlay.setVisibility(View.VISIBLE);
    		Log.i("Trailer", "Movie Clip Link = " + movie.getClipLink());
    	}
    	//if not go get the link from rotten tomoatoes
    	else{
    		//String trailerURL = TMDB_BASE_URL + "3/movie/" + id + "/trailers" + TMDB_API_KEY;
    		//release dates and trailers
    		new GetTrailerTask().execute(TMDB_BASE_URL + "3/movie/" + movie.getFullImdbId() + TMDB_API_KEY + "&append_to_response=releases,trailers");
    	}
        
        return info;
    }
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable("Movie", movie);
	}
	
	private class GetTrailerTask extends AsyncTask<String, Void, String[]>{
		
		@Override
	    protected void onPreExecute()
	    {
			vPlay.setVisibility(View.GONE);
	    }
	    	
	    @Override
	    protected String[] doInBackground(String... params) 
	    {
	    	if(params[0].contains("rottentomatoes")){
	    		String [] s = new String[1];
	    		s[0] = new OpenSocket(params[0]).getTrailerLink();
	    		return s;
	    	}
	    	else{
	    		return new OpenSocket(params[0]).getDateAndTrailer(movie.getFullImdbId(), MovieMeter.countryCode);
	    	}
	    }
	    
	    @Override
	    protected void onPostExecute(String [] results)
	    {
	    	if(results.length > 1){
		    	String date = results[0];
		    	String link = results[1];
		    	//Log.i("Trailer", "GetTrailerTask Link = " + link);
		    	
		    	//release date
		    	if(date != null && date.length() > 1){
		            movie.setReleaseDate(date);
		    		vReleaseDate.setText(movie.getLongReleaseDate());
		    		//update the Movie in the list if its not null
		    		if(MovieMeter.movieSelected != null){
			    		MovieMeter.movieSelected.setReleaseDate(date);
		    		}
		    	}
		    	//else we will use the date set already
		    	
		    	//trailer link
		    	if(link != null && link.length() > 0){
		    		movie.setClipLink(link);
		    		vPlay.setVisibility(View.VISIBLE);
		    		Log.i("Trailer", "GetTrailerTask Link = " + link);
		    		
		    		//update the Movie in the list if its not null
		    		if(MovieMeter.movieSelected != null){
		    			MovieMeter.movieSelected.setClipLink(link);
		    		}
		    	}
		    	else{ //if nothing returned then look for rotten tomatoes clip
		    		new GetTrailerTask().execute(MovieMeter.TRAILERS_URL_BASE + movie.getId() + "/clips.json?" + MovieMeter.API_KEY.substring(1, MovieMeter.API_KEY.length()));
		    	}
	    	}
	    	else{ //rottentomatoes - return link only
	    		String link = results[0];
	    		//trailer link
		    	if(link != null && link.length() > 0){
		    		movie.setClipLink(link);
		    		vPlay.setVisibility(View.VISIBLE);
		    		Log.i("Trailer", "GetTrailerTask Link = " + link);
		    		
		    		//update the Movie in the list if its not null
		    		if(MovieMeter.movieSelected != null){
		    			MovieMeter.movieSelected.setClipLink(link);
		    		}
		    	}
	    	}
	    }    
    }
}
