package com.movie.watch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;


public class DisplayMovie extends SherlockFragmentActivity{

	//Fragments
	private static final String[] CONTENT = new String[] { "Info", "Reviews", "Images", "Similar", "Showtimes"};
	
	public static final String MOVIE_URL_BASE = "http://api.rottentomatoes.com/api/public/v1.0/movies/"; //+ ":id" + REVIEW_FOLDER + API_KEY
	public static final String SIMILAR_MOVIES_URL_BASE = "/similar.json?limit=5"; //max limit allowed is 5
	
	protected static Movie movie;
	private static Bitmap poster;
	private String movieTitle;
	private static ArrayList<Movie> movies = new ArrayList<Movie>();

	//location vars
    protected static String locationCoordinates;
    
    //tmdb guest sesssion id
    //protected static String guestSessionId;
    
    //actionbar
    protected static ActionBar actionBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_header_layout);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        //retrieve movie value sent to this activity
        movie = getIntent().getParcelableExtra(MovieMeter.MOVIE_OBJECT);
        poster = getIntent().getParcelableExtra(MovieMeter.BITMAP_IMAGE);
        locationCoordinates = getIntent().getStringExtra(MovieMeter.LOCATION_STRING);
        
        movie.setPoster(poster);
        movieTitle = movie.getTitle();
        movies.add(movie);
        Log.d("log_tag", "Movie Added: Size of movies = " + movies.size());

        //set the title in the action bar
        setTitle(movieTitle);
               
        //ViewPager
        //MyPagerAdapter adapter = new MyPagerAdapter(movie);
        ViewPager myPager = (ViewPager) findViewById(R.id.movie_pager);
        myPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), movie));
        
        //Bind the title indicator to the adapter
        TabPageIndicator titleIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        titleIndicator.setViewPager(myPager);

      AdView mAdView = (AdView) findViewById(R.id.display_adView);
      AdRequest adRequest = new AdRequest.Builder().build();
      mAdView.loadAd(adRequest);
        
        myPager.setCurrentItem(0);
    }
    
    @Override
    public void onBackPressed() {
       Log.d("log_tag", "onBackPressed Called");
       if(movies.size() == 1){
    	   // last movie; go home
           Intent intent = new Intent(this, MovieMeter.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	        
	       //start main
           startActivity(intent);
       }
       else{
    	   //remove the last movie
    	   int size = movies.size() - 1;
    	   movies.remove(movies.get(size));
    	   Log.d("log_tag", "Movie Removed: Size of movies = " + movies.size());
    	   movie = movies.get(--size);
    	   super.onBackPressed();
       }
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	      super.onSaveInstanceState(savedInstanceState);
	      // Save UI state changes to the savedInstanceState.
	      // This bundle will be passed to onCreate if the process is
	      // killed and restarted.
	      savedInstanceState.putParcelable("Movie", movie);
	      savedInstanceState.putParcelable("Poster", poster);
	      savedInstanceState.putString("Location", locationCoordinates);
	      //savedInstanceState.putString("Guest ID", guestSessionId);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
      movie = savedInstanceState.getParcelable("Movie");
      poster = savedInstanceState.getParcelable("Poster");
      locationCoordinates = savedInstanceState.getString("Location");
      //guestSessionId = savedInstanceState.getString("Guest ID");
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
    	switch (item.getItemId()) 
    	{
	        case android.R.id.home:
	        {
	        	movies.removeAll(movies);
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MovieMeter.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	 	        
	 	       	//start main
	            startActivity(intent);
	            
	            return true;
	        }
	        default:
	        {
	        	return super.onOptionsItemSelected(item);
	        }
    	}
    }
    
    public static class MyPagerAdapter extends FragmentPagerAdapter{
    
	    private Movie movie;
	
	    public MyPagerAdapter(FragmentManager fm, Movie m) {      
	        super(fm);
	        this.movie = m;
	    }
	
	    @Override
	    public Fragment getItem(int position) {  
	        switch (position) {
		        case 0:              
		        	return MovieInfoFragment.newInstance(movie, poster);
		        case 1:
		            return ReviewsFragment.newInstance(movie);
		        case 2:
		        	return ImagesFragment.newInstance(movie);
		        case 3:
		            return SimilarFragment.newInstance(movie);
		        case 4:
		            return ShowtimesFragment.newInstance(movie);
	        }
	        return null;
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return CONTENT[position % CONTENT.length].toUpperCase();
	    }
	
	    @Override
	    public int getCount() {
	        return CONTENT.length;
	    }
	}
}
