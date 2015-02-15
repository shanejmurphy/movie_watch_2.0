package com.movie.watch;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class SimilarFragment extends Fragment{
	
	//resources
	private Drawable fresh;
	private Drawable rotten;
	private Drawable good;
	private Drawable bad;
		
	//instance vars
	private ArrayList<Movie> similarList = new ArrayList<Movie>();
	private ListView similarMoviesView;
	private LinearLayout similarProgress;
	private TextView noSimilar;
	private SimilarMoviesAdapter sAdapter;
	private String similarURL;
	private boolean isSimilarChecked = false;
	
	private Movie movie;
	
	static SimilarFragment newInstance(Movie m){
		SimilarFragment s = new SimilarFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("Movie", m);
		s.setArguments(args);
		
		return s;
	}
	
	@Override
	public void onAttach(Activity a){
		super.onAttach(a);
		sAdapter = new SimilarMoviesAdapter(a, R.layout.flipper, similarList);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			movie = getArguments().getParcelable("Movie");
			similarURL = DisplayMovie.MOVIE_URL_BASE + movie.getId() + DisplayMovie.SIMILAR_MOVIES_URL_BASE + MovieMeter.API_KEY;
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View similar = inflater.inflate(R.layout.main, null);
        
		//similar movies
        similarProgress = (LinearLayout) similar.findViewById(R.id.movie_progress_layout);
        similarProgress.setVisibility(View.GONE);
        
        similarMoviesView = (ListView) similar.findViewById(R.id.movie_list);        
        similarMoviesView.setAdapter(sAdapter);
        similarMoviesView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Movie movieSelected = similarList.get(position);
				Log.i("log_tag", "Title of Movie Clicked on is: " + movieSelected.getTitle());
				Intent intent = new Intent(getActivity(), DisplayMovie.class);
		        intent.putExtra(MovieMeter.MOVIE_OBJECT, movieSelected);
		        intent.putExtra(MovieMeter.BITMAP_IMAGE, movieSelected.getProfileBitmap());
		        if(DisplayMovie.locationCoordinates != null){
		        	intent.putExtra(MovieMeter.LOCATION_STRING, DisplayMovie.locationCoordinates);
		        }

		        startActivity(intent);	
			}
		});
        
        //reusing the main layout so we get duplicate adbar from main
        //need to hide this
        LinearLayout mainAdBar = (LinearLayout) similar.findViewById(R.id.adBar);
        mainAdBar.setVisibility(View.GONE);
        
        noSimilar = (TextView) similar.findViewById(R.id.no_similar);
        noSimilar.setVisibility(View.GONE);

        //drawables for the score
        fresh = getResources().getDrawable(R.drawable.fresh_small);
        rotten = getResources().getDrawable(R.drawable.rotten_small);
        good = getResources().getDrawable(R.drawable.popcorn_small);
        bad = getResources().getDrawable(R.drawable.bad_popcorn_small);
        
        return similar;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(movie.getSimilarMovies() == null){
        	if(isSimilarChecked == false){
        		//start thread for showtimes
            	new GetSimilarTask().execute(similarURL);
        	}
        	else{ //we have an empty list and we have already checked so show message
            	noSimilar.setVisibility(View.VISIBLE);
            }
        }
	}
	
	protected class SimilarMoviesAdapter extends ArrayAdapter<Movie>{
		
    	private Context context;
		public SimilarMoviesAdapter(Context context, int textViewResourceId, ArrayList<Movie> items)
		{
			super(context, textViewResourceId, items);
			similarList = items;
			this.context = context;
		}
		
		public void setList(ArrayList<Movie> items) {
			similarList.clear(); 
			similarList.addAll(items);
	        this.notifyDataSetChanged();
	    }
		
		public class ViewHolder{
			public TextView title;  
			public TextView consensus;
			public ImageView thumb;
			public TextView cScore;
			public TextView aScore;
			public LinearLayout progress;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			ViewHolder holder;
			if(v == null) 
			{  
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                v = vi.inflate(R.layout.flipper, null);
                holder = new ViewHolder();
                holder.title = (TextView) v.findViewById(R.id.title);
                holder.consensus = (TextView) v.findViewById(R.id.consensus);
                holder.thumb = (ImageView) v.findViewById(R.id.thumbnail);
                holder.cScore = (TextView) v.findViewById(R.id.critic_score);
                holder.aScore = (TextView) v.findViewById(R.id.audience_score);
                //Dont want this to show until loading more items
                holder.progress = (LinearLayout) v.findViewById(R.id.progress_layout);
                holder.progress.setVisibility(View.GONE);
                v.setTag(holder);
			}  
			else{
				holder =(ViewHolder) v.getTag();
			}
				
			//Movie
	        final Movie movie = similarList.get(position); 
        	String criticScore = Integer.toString(movie.getCriticScore()) + "%"; //convert integer to a string for display purposed
        	String audienceScore = Integer.toString(movie.getAudienceScore()) + "%"; //convert integer to a string for display purposed
	        String consensus = movie.getConsensus();
        	
	        if(movie != null){
	        	holder.title.setText(movie.getTitle());  
		        
		        //check consensus for ratings
		        if(consensus != ""){
		        	holder.consensus.setText(movie.getConsensus());
		        }
		        else {
		        	holder.consensus.setText("No Review Available");
		        }
		        
		        //holder.thumb.setImageBitmap(movie.getProfileBitmap());
		        
		        // Get singletone instance of ImageLoader
		        ImageLoader imageLoader = ImageLoader.getInstance();
		        // Initialize ImageLoader with configuration. Do it once.
		        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		        // Load and display image asynchronously
		        imageLoader.displayImage(movie.getProfileImg(), holder.thumb, MovieMeter.options, new ImageLoadingListener() {
		        	@Override
		            public void onLoadingStarted() {
		               //spinner.show();
		            }
		            @Override
		            public void onLoadingFailed(FailReason failReason) {
		                //spinner.hide();
		            }
		        	@Override
		            public void onLoadingComplete(Bitmap loadedImage) {
		                movie.setBitmap(loadedImage);
		            }
		            @Override
		            public void onLoadingCancelled() {
		                // Do nothing
		            }
		        });
		        
		        //critics
		        holder.cScore.setTextSize(18);
		        //holder.cScore.setTextColor(Color.rgb(94, 153, 45)); //rotten green
		        
		        //check for no ratings
		        if(criticScore.equals("-1%"))
		        {
		        	criticScore = "---";
		        	holder.cScore.setText(criticScore);
		        	holder.cScore.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
	
		        }
		        if(criticScore.equals("100%"))
		        {
		        	holder.cScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
		        	holder.cScore.setText(criticScore);
		        }
		        else if(movie.getCriticRating().contains("Fresh"))
		        {
		        	holder.cScore.setText(criticScore);     	
		        	holder.cScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
		        }
		        else
		        {
		        	holder.cScore.setText(criticScore);
		        	holder.cScore.setCompoundDrawablesWithIntrinsicBounds(rotten, null, null, null);
		        }
		        
		        holder.aScore.setText(audienceScore);
		        holder.aScore.setTextSize(18);
		        //holder.aScore.setTextColor(Color.rgb(94, 153, 45)); //rotten green
		        if(movie.getAudienceRating().equalsIgnoreCase("Upright") || movie.getAudienceScore() > 59)
		        { 
		        	holder.aScore.setCompoundDrawablesWithIntrinsicBounds(good, null, null, null);
		        }
		        else
		        {
		        	holder.aScore.setCompoundDrawablesWithIntrinsicBounds(bad, null, null, null);
		        }
	        }
	        return v;  
		}
	}
	
	private class GetSimilarTask extends AsyncTask<String, Void, ArrayList<Movie>>{
		
		@Override
	    protected void onPreExecute()
	    {
			noSimilar.setVisibility(View.GONE);
			similarProgress.setVisibility(View.VISIBLE);
	    }
	    	
	    @Override
	    protected ArrayList<Movie> doInBackground(String... params) 
	    {  	
	    	return new OpenSocket(params[0]).setMovies();
	    }
	    
	    @Override
	    protected void onPostExecute(ArrayList<Movie> moviesList)
	    {
	    	if(moviesList != null && moviesList.size() > 0){
	    		//similar movies
    			sAdapter.setList(moviesList);
    			movie.setSimilarMovies(moviesList);
	    	}
	    	else{
	    		similarList.clear();
    			sAdapter.clear();
    			sAdapter.notifyDataSetChanged();
    			noSimilar.setVisibility(View.VISIBLE);
			}
	    	similarProgress.setVisibility(View.GONE);
	    	isSimilarChecked = true;
	    }   
	}
}
