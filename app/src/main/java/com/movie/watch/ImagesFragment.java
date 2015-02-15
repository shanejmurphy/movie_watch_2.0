package com.movie.watch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ImagesFragment extends Fragment{
	
	//final vars
	private static final String TMDB_IMAGE_BASE_URL = "http://cf2.imgobject.com/t/p/w300";
	
	//instance vars
	private ImageAdapter iAdapter;
	private ArrayList<String> imageList = new ArrayList<String>();
	private Movie movie;
	private boolean isImagesChecked = false;
	
	//layout
	private LinearLayout imagesProgress;
	private TextView noImages;;
	
	//options for displayng images
    private DisplayImageOptions imageOptions;
	
	static ImagesFragment newInstance(Movie movie) {
		ImagesFragment r = new ImagesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("Movie", movie);
        r.setArguments(args);

        return r;
    }
	
	@Override
	public void onAttach(Activity a){
		super.onAttach(a);
		iAdapter = new ImageAdapter(a, R.layout.movie_images_list_layout, imageList);
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(getArguments() != null){
			movie = getArguments().getParcelable("Movie");
		}
		
		//imageloader options -- more available
		imageOptions = new DisplayImageOptions.Builder()
    	.resetViewBeforeLoading()
        .cacheInMemory()
        .cacheOnDisc()
        .build();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//Inflate the layout for this fragment
		View images = inflater.inflate(R.layout.movie_images_layout, null);
		
		//initialise the progressbar for the images
		imagesProgress = (LinearLayout) images.findViewById(R.id.images_progress_layout);
		imagesProgress.setVisibility(View.GONE);
		
		//no images textview
		noImages = (TextView) images.findViewById(R.id.no_images);
		noImages.setVisibility(View.GONE);
        
        //list view resides in the setContentView file
        ListView listView = (ListView) images.findViewById(R.id.images_list); 
        
        //Reviews
        listView.setAdapter(iAdapter);
		
		return images;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if(imageList == null || imageList.isEmpty()){
        	if(isImagesChecked == false){
        		//start thread for showtimes
        		new GetImagesTask().execute(MovieInfoFragment.TMDB_BASE_URL + "3/movie/" + movie.getFullImdbId() + "/images" + MovieInfoFragment.TMDB_API_KEY);
        		isImagesChecked = true;
        	}
        	else{
        		noImages.setVisibility(View.VISIBLE);
        	}
        }
	}
	
	private class ImageAdapter extends ArrayAdapter<String>{
		private Context context;
		
		public ImageAdapter(Context context, int textViewResourceId, ArrayList<String> items)
		{
			super(context, textViewResourceId, items);
			this.context = context;
			imageList = items;
		}
		
		public void setList(List<String> items) {
			imageList.clear(); 
			imageList.addAll(items);
	        this.notifyDataSetChanged();
	    }
		
		@Override
		public View getView(int position, View convertView, ViewGroup Parent)
		{
			View v = convertView;
			if(v == null) 
			{  
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                v = vi.inflate(R.layout.movie_images_list_layout, null);  
			}  
			
	        String s = TMDB_IMAGE_BASE_URL + imageList.get(position); 
	        //Log.i("Images", "Image URL = " + s);
	        
	        //assign variables to the various views in the layout_list.xml file
	        ImageView vImage = (ImageView) v.findViewById(R.id.image);  
	        
	        if(imageList.size() > 0){
		        //name
	        	// Get singletone instance of ImageLoader
		        ImageLoader imageLoader = ImageLoader.getInstance();
		        // Initialize ImageLoader with configuration. Do it once.
		        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		        // Load and display image asynchronously
		        imageLoader.displayImage(s, vImage, imageOptions, new ImageLoadingListener() {
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
		        		// Do nothing
		            }
		            @Override
		            public void onLoadingCancelled() {
		                // Do nothing
		            }
		        });
	        }

	        return v;  
		}
	}
	
	private class GetImagesTask extends AsyncTask<String, Void, ArrayList<String>>{
		@Override
	    protected void onPreExecute()
	    {
			imagesProgress.setVisibility(View.VISIBLE);
			noImages.setVisibility(View.GONE);
	    }
	    	
	    @Override
	    protected ArrayList<String> doInBackground(String... params) 
	    {
	    	return new OpenSocket(params[0]).getImages();
	    }
	    
	    @Override
	    protected void onPostExecute(ArrayList<String> images)
	    {
	    	if(images != null && images.size() > 0){
	    		imageList.addAll(images);
	    		iAdapter.setList(images);
	    	}
	    	else
	    	{
	    		noImages.setVisibility(View.VISIBLE);
	    	}
	    	
	    	imagesProgress.setVisibility(View.GONE);
	    }   
	}
}
