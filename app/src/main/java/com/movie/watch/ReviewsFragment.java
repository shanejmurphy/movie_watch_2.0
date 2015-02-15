package com.movie.watch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

public class ReviewsFragment extends Fragment{
	
	public static final String REVIEW_FOLDER = "/reviews.json?review_type=top_critic&page_limit=16&page=1&country=us";
	
	private Movie movie;
	private String reviewURL;
	//private Context context;
	
	//resources
	private Drawable fresh;
	private Drawable rotten;
	
	//Layout
	private LinearLayout reviewsProgress;
	private TextView vNoReview;
	
	private ReviewListAdapter rAdapter;
	private ArrayList<Review> reviewList = new ArrayList<Review>();
	
	
	/**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static ReviewsFragment newInstance(Movie movie) {
    	ReviewsFragment r = new ReviewsFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("Movie", movie);
        r.setArguments(args);

        return r;
    }
	
	@Override
	public void onAttach(Activity a){
		super.onAttach(a);
		rAdapter = new ReviewListAdapter(a, R.layout.critic_review_list, reviewList);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			movie = getArguments().getParcelable("Movie");
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//Inflate the layout for this fragment
		View reviews = inflater.inflate(R.layout.movie_review_layout, null);	
		
		//initialise the progressbar for the reviews
		reviewsProgress = (LinearLayout) reviews.findViewById(R.id.reviews_progress_layout);
		vNoReview = (TextView) reviews.findViewById(R.id.no_review);
        
        //list view resides in the setContentView file
        ListView listView = (ListView) reviews.findViewById(R.id.critics_list); 
        
        //Reviews
        listView.setAdapter(rAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				try{
					final Uri uri = Uri.parse(DisplayMovie.movie.getReviews().get(position).getLink()); 
					//Log.d("log_tag", "Movie Title = " + movie.getTitle());
					//Log.d("log_tag", "Movie Reviews Size = " + movie.getReviews().size());
					//Log.d("log_tag", "Movie Review Link = " + movie.getReviews().get(position).getLink());
					getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
				}catch(Exception e){
					Log.e("error_log", "ArrayIndexOutOfBoundsException " + e.toString());
				}
			}
		}); 
        
        //set the URL for the reviews
        reviewURL = DisplayMovie.MOVIE_URL_BASE + movie.getId() + REVIEW_FOLDER + MovieMeter.API_KEY;
        //drawables for the score
        fresh = getResources().getDrawable(R.drawable.fresh_small);
        rotten = getResources().getDrawable(R.drawable.rotten_small);
        
        return reviews;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(movie.getReviews().isEmpty()){
        	//start thread for showtimes
        	new GetCriticReviewsTask().execute(reviewURL);
        }
	}
	
	class ReviewListAdapter extends ArrayAdapter<Review>
	{
		//private ArrayList<Review> reviews;
		public ReviewListAdapter(Context context, int textViewResourceId, ArrayList<Review> items)
		{
			super(context, textViewResourceId, items);
			reviewList = items;
		}
		
		public void setList(List<Review> items) {
			reviewList.clear(); 
			reviewList.addAll(items);
	        this.notifyDataSetChanged();
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup Parent)
		{
			View v = convertView;
			if(v == null) 
			{  
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                v = vi.inflate(R.layout.critic_review_list, null);  
			}  
			
	        Review r = reviewList.get(position);  
	        
	        //assign variables to the various views in the layout_list.xml file
	        TextView vName = (TextView) v.findViewById(R.id.critic_name);  
	        TextView vPub = (TextView) v.findViewById(R.id.critic_publication); 
	        ImageView vRating = (ImageView) v.findViewById(R.id.critic_review_rating);
	        TextView vDate = (TextView) v.findViewById(R.id.critic_date);
	        TextView vQuote = (TextView) v.findViewById(R.id.critic_quote);
	        

	        if(reviewList.size() > 0){
		        //name
		        vName.setText(r.getCriticName());  
		        
		        //publication
		        vPub.setText(r.getPublication());
		        
		        //image
		        if(r.getRating().equalsIgnoreCase("fresh")){
		        	vRating.setBackgroundDrawable(fresh);
		        }
		        else{
		        	vRating.setBackgroundDrawable(rotten);
		        }
		        
		        //quote
		        vQuote.setText(r.getQuote());
		        
		        //date
		        vDate.setText(r.getLongDate());
	        }
	        else
	        {
	        	vName.setText("No Reviews Available!");
	        }

	        return v;  
		}
	}
	
	private class GetCriticReviewsTask extends AsyncTask<String, Void, ArrayList<Review>>{
		
		@Override
	    protected void onPreExecute()
	    {
			vNoReview.setVisibility(View.GONE);
			reviewsProgress.setVisibility(View.VISIBLE);
	    }
	    	
	    @Override
	    protected ArrayList<Review> doInBackground(String... params) 
	    {
	    	return new OpenSocket(params[0]).getReviews();
	    }
	    
	    @Override
	    protected void onPostExecute(ArrayList<Review> reviews)
	    {
	    	if(reviews != null && reviews.size() > 0){
	    		movie.setReviews(reviews);
	    		rAdapter.setList(reviews);
	    	}
	    	else
	    	{
	    		reviewList.clear();
    			rAdapter.clear();
    			rAdapter.notifyDataSetChanged();
	    		vNoReview.setVisibility(View.VISIBLE);
	    	}
	    	
	    	reviewsProgress.setVisibility(View.GONE);
	    }    
    }
}
