package com.movie.watch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.app.ToolbarActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.movie.watch.MyCurrentLocation.LocationResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieMeter extends ActionBarActivity {
  private ArrayList<Movie> movieList = new ArrayList<Movie>();
  private boolean mIsLoading = false;
  private MovieListAdapter mAdapter;

  //Drawables for the rating info
  //rotten tomatoes
  protected static Drawable fresh;
  protected static Drawable rotten;

  //flixter
  protected static Drawable good;
  protected static Drawable bad;

  //main url
  public static final String MOVIE_URL_BASE = "http://api.rottentomatoes.com/api/public/v1.0/lists/"; //box_office.json?limit=16&country=us&apikey=wtcku8jhpcj3zh6gtexnb94x";

  //movie folders
  public static final String BOX_OFFICE = "movies/box_office.json?limit=16";
  public static final String IN_THEATRES = "movies/in_theaters.json?page_limit=16";//&page=1";
  public static final String OPENING = "movies/opening.json?limit=16";
  public static final String UPCOMING_MOVIES = "movies/upcoming.json?page_limit=16";//&page=1";

  //dvd folders
  public static final String TOP_RENTALS = "dvds/top_rentals.json?limit=16";
  public static final String CURRENT_RELEASES = "dvds/current_releases.json?page_limit=16";//&page=1";
  public static final String NEW_RELEASES = "dvds/new_releases.json?page_limit=16";//&page=1";
  public static final String UPCOMING_DVDS = "dvds/upcoming.json?page_limit=16";//&page=1";

  //search Url
  public static final String BASE_SEARCH_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=";

  //trailers url
  public static final String TRAILERS_URL_BASE = "http://api.rottentomatoes.com/api/public/v1.0/movies/";
  //api key -- append to above urls
  public static final String API_KEY = "&apikey=wtcku8jhpcj3zh6gtexnb94x";

  //parcelable strings
  public static final String MOVIE_OBJECT = "com.movie.watch.MOVIE";
  public static final String BITMAP_IMAGE = "bitmap";
  public static final String LOCATION_STRING = "Location";

  //for expanding lists
  private static final int VISIBLE_THRESHOLD = 4;

  //movie lists
  public static ArrayList<Movie> boxOffice = new ArrayList<Movie>();
  public static ArrayList<Movie> inTheatres = new ArrayList<Movie>();
  public static ArrayList<Movie> opening = new ArrayList<Movie>();
  public static ArrayList<Movie> comingSoon = new ArrayList<Movie>();
  public static ArrayList<Movie> rentals = new ArrayList<Movie>();
  public static ArrayList<Movie> currentReleases = new ArrayList<Movie>();
  public static ArrayList<Movie> newReleases = new ArrayList<Movie>();
  public static ArrayList<Movie> releasedSoon = new ArrayList<Movie>();
  public static ArrayList<Movie> searchList = new ArrayList<Movie>();

  //page flipping variable
  private int[] listPage;
  private int currentListPage;
  private int currentPage;
  private int previousTotal;
  private int dummySpinnerPage;

  private boolean isSearching = false;

  //Location variable
  protected static final String country = "us";
  protected static String countryCode = "us";

  //class vars
  private GetMovieInfoTask movieInfo;
  private GetMoreMovieInfoTask moreMovieInfo;
  private ListView lv;
  private LinearLayout moviesProgress;
  private View.OnTouchListener gestureListener;
  private int storedPreference;
  protected static Movie movieSelected;

  //actionbar
  private ToolbarActionBar actionBar;

  //options for displayng images
  protected static DisplayImageOptions options;

  //location vars
  private MyCurrentLocation myCurrentLocation;
  private LocationResult locationResult;
  private double latitiude;
  private double longitude;
  protected static String locationCoordinates;

  private static boolean isCountryChecked = false;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

		/*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

    if (savedInstanceState != null) {
      // Restore last state for checked position.
      movieList = savedInstanceState.getParcelableArrayList("MovieList");
      //Log.i("log_tag", "Restored Movie List = " + movieList.get(0).getTitle());
    }

    setContentView(R.layout.main);

    AdView mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    //pages array for those categories that have paginated results
    listPage = new int[]{0, 1, 0, 1, 0, 1, 1, 1};

    //drawables
    //rotten tomatoes
    fresh = getResources().getDrawable(R.drawable.fresh_small);
    rotten = getResources().getDrawable(R.drawable.rotten_small);

    //flixter
    good = getResources().getDrawable(R.drawable.popcorn_small);
    bad = getResources().getDrawable(R.drawable.bad_popcorn_small);
    	
    	/*country = this.getResources().getConfiguration().locale.getCountry();
    	if(country.equalsIgnoreCase("gb") || country.equalsIgnoreCase("ie")){
    		country = "uk";
    	}
    	else
    	{
    		country = country.toLowerCase();
    	}
    	Log.i("country_tag", country);
    	*/
    //country = "us";
/*
    actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setDisplayShowHomeEnabled(true);*/

    //initialise Views from main
    lv = (ListView) findViewById(R.id.movie_list);
    moviesProgress = (LinearLayout) findViewById(R.id.movie_progress_layout);

    //set customised adapter for list
    mAdapter = new MovieListAdapter(this, R.layout.flipper, movieList);
    lv.setAdapter(mAdapter);

    //create gesture listener and click listener for lv
    MyGestureDetector myGestureDetector = new MyGestureDetector(VISIBLE_THRESHOLD);

    final GestureDetector gestureDetector = new GestureDetector(this, myGestureDetector);
    gestureListener = new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
      }
    };
    lv.setOnScrollListener(myGestureDetector);//.setOnTouchListener(gestureListener);
    lv.setOnTouchListener(gestureListener);

    //Actionbar Spinner code
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    storedPreference = preferences.getInt("storedInt", 0);

    SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_list, R.layout.spinner_layout);
    /*OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

      SharedPreferences navPreferences = PreferenceManager.getDefaultSharedPreferences(MovieMeter.this);

      @Override
      public boolean onNavigationItemSelected(int position, long itemId) {

        Log.d("nav_tag", position + " selected");
        SharedPreferences.Editor editor = navPreferences.edit();
        editor.putInt("storedInt", position); // value to store
        editor.commit();

        //display the list for selected item
        getListToDisplay(position);
        return true;
      }
    };

    actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    actionBar.setSelectedNavigationItem(storedPreference);
    */

    //imageloader options -- more available
    options = new DisplayImageOptions.Builder()
    .showStubImage(R.drawable.black_bg)
    .resetViewBeforeLoading()
    .cacheInMemory()
    .cacheOnDisc()
    .build();

    //hide keyboard
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    //location infromation
    myCurrentLocation = new MyCurrentLocation();
    locationResult = new LocationResult() {
      @Override
      public void gotLocation(Location location) {
        if (location != null) {
          //Got the location!
          latitiude = location.getLatitude();
          longitude = location.getLongitude();
          locationCoordinates = Double.toString(latitiude) + "," + Double.toString(longitude);
          //myLocation = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
          Log.d("Location", "Location Found (" + locationCoordinates + ")");
          //we got an update so stop listening
          myCurrentLocation.removeListenerUpdates();

          //now translate into country code using geocoder
          if (isCountryChecked == false)
            getAddressFromLocation(location, MovieMeter.this, new GeocoderHandler());
        }
      }
    };
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putParcelableArrayList("MovieList", movieList);
  }

  @Override
  protected void onStart() {
    super.onStart();
    myCurrentLocation.getNetworkLocation(this, locationResult);
    Log.d(LOCATION_STRING, "MovieMeter onStart Event: Listener Updates Started");
  }

  /**
   * Stop the updates when Activity is stopped
   */
  @Override
  protected void onStop() {
    super.onStop();
    myCurrentLocation.removeListenerUpdates();
    Log.d(LOCATION_STRING, "MovieMeter onStop Event: Listener Updates Removed");
  }

  /*//search menu
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    item.setOnActionExpandListener(new OnActionExpandListener() {

      @Override
      public boolean onMenuItemActionExpand(MenuItem item) {
        // TODO Auto-generated method stub
        //var to remember last used category to return to from search
        dummySpinnerPage = (getResources().getStringArray(R.array.spinner_list).length - 1) - currentListPage;
        //Log.d("nav_tag", "dummySpinnerPage = " + dummySpinnerPage);
        return true;
      }

      @Override
      public boolean onMenuItemActionCollapse(MenuItem item) {
        //return to correct page
        int correctPage = (getResources().getStringArray(R.array.spinner_list).length - 1) - dummySpinnerPage;
        //Log.d("nav_tag", "dummyPage = " + correctPage);
        getListToDisplay(correctPage); //display list
        return true;
      }
    });

    // Handle item selection
    switch (item.getItemId()) {
      case R.id.search: {
        onSearchRequested();
        isSearching = true;
        return true;
      }
      case R.id.about: {
        //create an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View about = li.inflate(R.layout.about_dialog, null);
        builder.setTitle(R.string.notice);
        builder.setView(about);
        builder.setCancelable(true);
        builder.show();
      }
      default:
        return super.onOptionsItemSelected(item);
    }
  }*/

  private static class GeocoderHandler extends Handler {
    @Override
    public void handleMessage(Message message) {
      String result;
      switch (message.what) {
        case 1:
          Bundle bundle = message.getData();
          result = bundle.getString("Country");
          break;
        default:
          result = "us";
      }
      Log.i("Country", "Country code is " + result);
      // replace by what you need to do
      countryCode = result;
      isCountryChecked = true;
    }
  }

  //geocoder
  public static void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
    Thread thread = new Thread() {
      @Override
      public void run() {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
          List<Address> list = geocoder.getFromLocation(
          location.getLatitude(), location.getLongitude(), 1);
          if (list != null && list.size() > 0) {
            Address address = list.get(0);
            // sending back first address line and locality
            //result = address.getAddressLine(0) + ", " + address.getLocality();
            result = address.getCountryCode();
          }
        } catch (IOException e) {
          Log.e("Geocoder", "Impossible to connect to Geocoder", e);
        } finally {
          Message msg = Message.obtain();
          msg.setTarget(handler);
          if (result != null) {
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("Country", result);
            msg.setData(bundle);
          } else
            msg.what = 0;
          msg.sendToTarget();
        }
      }
    };
    thread.start();
  }

/*  @SuppressLint({"NewApi", "NewApi", "NewApi"})
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getSupportMenuInflater();
    inflater.inflate(R.menu.search_activity, menu);

    //cater for newer versions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
      SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
      searchView.setIconifiedByDefault(false);
    }

    return true;
  }*/

  public void setIsLoading(boolean isLoading) {
    if (mIsLoading != isLoading) {
      mIsLoading = isLoading;
      mAdapter.notifyDataSetChanged();
    }
  }

  // Do not use LitView.setOnItemClickListener(). Instead, I override
  // SimpleOnGestureListener.onSingleTapUp() method, and it will call to this method when
  // it detects a tap-up event.
  private void myOnItemClick(int position) {
    try {
      if (!movieList.isEmpty()) {
        movieSelected = movieList.get(position);
        Intent intent = new Intent(MovieMeter.this, DisplayMovie.class);
        intent.putExtra(MOVIE_OBJECT, movieSelected);
        intent.putExtra(BITMAP_IMAGE, movieSelected.getProfileBitmap());
        if (locationCoordinates != null) {
          intent.putExtra(LOCATION_STRING, locationCoordinates);
        } else {
          myCurrentLocation.updateWithLastLocation();
          intent.putExtra(LOCATION_STRING, locationCoordinates);
        }
        startActivity(intent);
      }
    } catch (Exception e) {
      Log.e("log_tag", "Exception caught when selecting movie from list " + e.toString());
    }
  }

  public void getListToDisplay(int position) {
    currentListPage = position; //set the list variable for scrolling
    isSearching = false;

    //reset the list before showing new info
    mAdapter.clear();
    mAdapter.notifyDataSetChanged();
    switch (position) {
      case (0): {
        //Top Box Office
        if (boxOffice.size() > 0) {
          mAdapter.setList(boxOffice);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + BOX_OFFICE + "&country=" + country + API_KEY);
        }
        break;
      }
      case (1): {
        //in thetares
        if (inTheatres.size() > 0) {
          mAdapter.setList(inTheatres);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + IN_THEATRES + "&page=1&country=" + country + API_KEY);
          //refresh vars for scrolling
          listPage[currentListPage] = 1;
          currentPage = listPage[currentListPage];
          previousTotal = (listPage[currentListPage] - 1) * 16;
        }
        break;
      }
      case (2): {
        //Opening
        if (opening.size() > 0) {
          mAdapter.setList(opening);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + OPENING + "&country=" + country + API_KEY);
        }
        break;
      }
      case (3): {
        //coming soon
        if (comingSoon.size() > 0) {
          mAdapter.setList(comingSoon);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + UPCOMING_MOVIES + "&page=1&country=" + country + API_KEY);
          //refresh vars for scrolling
          listPage[currentListPage] = 1;
          currentPage = listPage[currentListPage];
          previousTotal = (listPage[currentListPage] - 1) * 16;
        }
        break;
      }
      case (4): {
        //Top rentals
        if (rentals.size() > 0) {
          mAdapter.setList(rentals);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + TOP_RENTALS + "&country=" + country + API_KEY);
        }
        break;
      }
      case (5): {
        //current releases
        if (currentReleases.size() > 0) {
          mAdapter.setList(currentReleases);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + CURRENT_RELEASES + "&page=1&country=" + country + API_KEY);
          //refresh vars for scrolling
          listPage[currentListPage] = 1;
          currentPage = listPage[currentListPage];
          previousTotal = (listPage[currentListPage] - 1) * 16;
        }
        break;
      }
      case (6): {
        //new releases
        if (newReleases.size() > 0) {
          mAdapter.setList(newReleases);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + NEW_RELEASES + "&country=" + country + API_KEY);
          //refresh vars for scrolling
          listPage[currentListPage] = 1;
          currentPage = listPage[currentListPage];
          previousTotal = (listPage[currentListPage] - 1) * 16;
        }
        break;
      }
      case (7): {
        //coming soon
        if (releasedSoon.size() > 0) {
          mAdapter.setList(releasedSoon);
        } else {
          movieInfo = new GetMovieInfoTask(moviesProgress, mAdapter);
          movieInfo.execute(MOVIE_URL_BASE + UPCOMING_DVDS + "&page=1&country=" + country + API_KEY);
          //refresh vars for scrolling
          listPage[currentListPage] = 1;
          currentPage = listPage[currentListPage];
          previousTotal = (listPage[currentListPage] - 1) * 16;
        }
        break;
      }
    }
  }

  private class MyGestureDetector extends SimpleOnGestureListener implements OnScrollListener {

    private int visibleThreshold;
    private boolean loading = true;

    //constructor with 1 arg
    public MyGestureDetector(int visibleThreshold) {
      this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
      if (!isSearching && (currentListPage == 1 || currentListPage == 5 || currentListPage == 6 || currentListPage == 7)) {
        if (loading) {
          if (totalItemCount > previousTotal) {
            loading = false;
          }
        }
        if (!loading && ((totalItemCount - visibleItemCount) < (firstVisibleItem + visibleThreshold)) && ((totalItemCount - visibleItemCount) > visibleThreshold)) {

          //make sure extra pages dont load when not required
          if (totalItemCount % 16 == 0) {

            //increment variables
            listPage[currentListPage]++;
            currentPage = listPage[currentListPage];
            previousTotal = (listPage[currentListPage] - 1) * 16;

            //add more movies to list and display
            //In Theatres
            if (currentListPage == 1 && currentPage < 4) { //48 movies
              moreMovieInfo = new GetMoreMovieInfoTask();
              moreMovieInfo.execute(MOVIE_URL_BASE + IN_THEATRES + "&page=" + currentPage + "&country=" + country + API_KEY);
              loading = true;
            }
            //current releases
            else if (currentListPage == 5 && currentPage < 4) {
              moreMovieInfo = new GetMoreMovieInfoTask();
              moreMovieInfo.execute(MOVIE_URL_BASE + CURRENT_RELEASES + "&page=" + currentPage + "&country=" + country + API_KEY);
              loading = true;
            }
            //new releases
            else if (currentListPage == 6 && currentPage < 4) {
              moreMovieInfo = new GetMoreMovieInfoTask();
              moreMovieInfo.execute(MOVIE_URL_BASE + NEW_RELEASES + "&page=" + currentPage + "&country=" + country + API_KEY);
              loading = true;
            }
            //upcoming dvds
            else if (currentListPage == 7 && currentPage < 4) {
              moreMovieInfo = new GetMoreMovieInfoTask();
              moreMovieInfo.execute(MOVIE_URL_BASE + UPCOMING_DVDS + "&page=" + currentPage + "&country=" + country + API_KEY);
              loading = true;
            }
          }
        }
        //Log.d("Scroll:", "((totalItemCount - visibleItemCount) < (firstVisibleItem + visibleThreshold)) && ((totalItemCount - visibleItemCount) > visibleThreshold)");
        //Log.d("Scroll:", "((" + Integer.toString(totalItemCount) + " - " + Integer.toString(visibleItemCount) + " < " + "(" + Integer.toString(firstVisibleItem) + " + " + Integer.toString(visibleThreshold) + ") && ((" + Integer.toString(totalItemCount) + " - " + Integer.toString(visibleItemCount) + " > " + Integer.toString(visibleThreshold) + ")");
        //Log.d("Scroll:", "mIsLoading = " + Boolean.toString(mIsLoading));
        //Log.d("Scroll:", "visibleItemCount = " + Integer.toString(visibleItemCount));
        //Log.d("Scroll:", "totalItemCount = " + Integer.toString(totalItemCount));
        //Log.d("Scroll:", "firstVisibleItem = " + Integer.toString(firstVisibleItem));
        //Log.d("Scroll:", "previousTotal = " + Integer.toString(previousTotal));
        //Log.d("Scroll:", "currentPage = " + Integer.toString(currentPage));
        //Log.d("Scroll:", "listPage[currentListPage] = " + Integer.toString(listPage[currentListPage]));
      }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
      int pos = lv.pointToPosition((int) e.getX(), (int) e.getY());
      myOnItemClick(pos);
      return false;
    }
  }

  //Custom adapter for movies
  protected class MovieListAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;
    private Context context;

    public MovieListAdapter(Context c, int textViewResourceId, ArrayList<Movie> items) {
      super(c, textViewResourceId, items);
      this.context = c;
      this.movies = items;
    }

    public class ViewHolder {
      public TextView title;
      public TextView consensus;
      public ImageView thumb;
      public TextView cScore;
      public TextView aScore;
      public LinearLayout progress;
    }

    public void setList(ArrayList<Movie> items) {
      this.movies.clear();
      this.movies.addAll(items);
      mAdapter.notifyDataSetChanged();
    }

    //method for adding more items to the list
    public void addToList(ArrayList<Movie> items) {
      this.movies.addAll(items);
      mAdapter.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return super.getCount() + (mIsLoading ? 1 : 0); //if misLoading add 1 on to the number of items in the list
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View v = convertView;
      ViewHolder holder;
      if (v == null) {
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
      } else {
        holder = (ViewHolder) v.getTag();
      }

      if (mIsLoading && position == (getCount() - 1)) { // last position -- 16
        holder.progress.setVisibility(View.VISIBLE);
      } else {

        holder.progress.setVisibility(View.GONE);

        //Movie
        final Movie movie = this.movies.get(position);
        String criticScore = Integer.toString(movie.getCriticScore()) + "%"; //convert integer to a string for display purposed
        String audienceScore = Integer.toString(movie.getAudienceScore()) + "%"; //convert integer to a string for display purposed
        //String consensus = movie.getConsensus();
        String consensus = movie.getSynopsis();

        if (movie != null) {
          holder.title.setText(movie.getTitle());

          //check consensus for ratings
          if (consensus != "") {
            //holder.consensus.setText(movie.getConsensus());
            holder.consensus.setText(movie.getSynopsis());
          } else {
            holder.consensus.setText("No Review Available");
          }

          //holder.thumb.setImageBitmap(movie.getProfileBitmap());

          // Get singletone instance of ImageLoader
          ImageLoader imageLoader = ImageLoader.getInstance();
          // Initialize ImageLoader with configuration. Do it once.
          imageLoader.init(ImageLoaderConfiguration.createDefault(context));
          // Load and display image asynchronously
          imageLoader.displayImage(movie.getProfileImg(), holder.thumb, options, new ImageLoadingListener() {
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
          if (criticScore.equals("-1%")) {
            criticScore = "---";
            holder.cScore.setText(criticScore);
            holder.cScore.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

          }
          if (criticScore.equals("100%")) {
            holder.cScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
            holder.cScore.setText(criticScore);
          } else if (movie.getCriticScore() > 59) {
            holder.cScore.setText(criticScore);
            holder.cScore.setCompoundDrawablesWithIntrinsicBounds(fresh, null, null, null);
          } else {
            holder.cScore.setText(criticScore);
            holder.cScore.setCompoundDrawablesWithIntrinsicBounds(rotten, null, null, null);
          }

          holder.aScore.setText(audienceScore);
          holder.aScore.setTextSize(18);
          //holder.aScore.setTextColor(Color.rgb(94, 153, 45)); //rotten green
          if (movie.getAudienceScore() > 59) {
            holder.aScore.setCompoundDrawablesWithIntrinsicBounds(good, null, null, null);
          } else {
            holder.aScore.setCompoundDrawablesWithIntrinsicBounds(bad, null, null, null);
          }
        }
      }

      return v;
    }
  }

  private class GetMoreMovieInfoTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private String urlString;

    @Override
    protected void onPreExecute() {
      setIsLoading(true);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
      urlString = params[0];
      return new OpenSocket(params[0]).setMovies();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesList) {
      if (moviesList != null && moviesList.size() > 0) {

        mAdapter.addToList(moviesList);

        if (urlString.contains(BOX_OFFICE)) {
          boxOffice.addAll(moviesList);
        } else if (urlString.contains(IN_THEATRES)) {
          inTheatres.addAll(moviesList);
        } else if (urlString.contains(OPENING)) {
          opening.addAll(moviesList);
        } else if (urlString.contains(UPCOMING_MOVIES)) {
          comingSoon.addAll(moviesList);
        } else if (urlString.contains(TOP_RENTALS)) {
          rentals.addAll(moviesList);
        } else if (urlString.contains(CURRENT_RELEASES)) {
          currentReleases.addAll(moviesList);
        } else if (urlString.contains(NEW_RELEASES)) {
          newReleases.addAll(moviesList);
        } else if (urlString.contains(UPCOMING_DVDS)) {
          releasedSoon.addAll(moviesList);
        }
      }
      setIsLoading(false);
    }
  }


  //Search stuff
  @Override
  protected void onNewIntent(Intent intent) {
    setIntent(intent);
    handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      Log.i("search_tag", "Typed Query = " + query);
      searchMovies(query);
    }

  }

  public void searchMovies(String term) {
    String searchURL = BASE_SEARCH_URL + term + "&page_limit=16&page=1" + MovieMeter.API_KEY;
    //Log.i("search_tag", "Search URL= " + searchURL);

    try {
      URL url = new URL(searchURL);
      URI uri = new URI(url.getProtocol(), url.getAuthority(), url.getPath(), url.getQuery(), null);
      searchURL = uri.toString();
      Log.i("search_tag", "Modified URi = " + searchURL);
    } catch (Exception e) {
      Log.e("error_tag", "Malformed URL Expression" + e.toString());
    }

    new SearchMovieTask().execute(searchURL);
  }

  private class SearchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    @Override
    protected void onPreExecute() {
      mAdapter.clear();
      searchList.clear();
      moviesProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
      return new OpenSocket(params[0]).setMovies();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> moviesList) {
      moviesProgress.setVisibility(View.GONE);
      mAdapter.setList(moviesList);
      if (moviesList != null && moviesList.size() > 0) {
        searchList.addAll(moviesList);
      }
      //get rid of keyboard
      lv.requestFocus();
    }

  }
}
