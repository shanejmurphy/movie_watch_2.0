package com.movie.watch.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.movie.watch.R;
import com.movie.watch.busevents.BoxOfficeFetchEvent;
import com.movie.watch.busevents.ComingSoonFetchEvent;
import com.movie.watch.busevents.CreditsErrorEvent;
import com.movie.watch.busevents.CreditsFetchEvent;
import com.movie.watch.busevents.CurrentReleasesFetchEvent;
import com.movie.watch.busevents.FetchErrorEvent;
import com.movie.watch.busevents.GoogleAddressFetchEvent;
import com.movie.watch.busevents.InTheatresFetchEvent;
import com.movie.watch.busevents.NewReleasesFetchEvent;
import com.movie.watch.busevents.OpeningFetchEvent;
import com.movie.watch.busevents.ReviewsErrorEvent;
import com.movie.watch.busevents.ReviewsFetchEvent;
import com.movie.watch.busevents.SearchErrorEvent;
import com.movie.watch.busevents.SearchResultsFetchEvent;
import com.movie.watch.busevents.ShowtimesErrorEvent;
import com.movie.watch.busevents.ShowtimesFetchEvent;
import com.movie.watch.busevents.TmdbFindMovieEvent;
import com.movie.watch.busevents.TmdbGetMovieEvent;
import com.movie.watch.busevents.TopRentalsFetchEvent;
import com.movie.watch.busevents.UpcomingFetchEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.model.MovieListType;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.address.Address;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;
import com.movie.watch.utils.MovieFetcher;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

import java.net.URLEncoder;

import de.greenrobot.event.EventBus;

@EIntentService
public class MovieFetchingService extends IntentService {
  private static final String TAG = MovieFetchingService.class.getSimpleName();

  @Bean
  protected MovieFetcher movieFetcher;
  @Bean
  protected MovieInfoParser movieInfoParser;

  public MovieFetchingService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //do nothing - not required when using annotations
  }

  @ServiceAction
  protected void fetchMovies(MovieListType listType, String listName, int page) {
    try {
      MovieList movieList = movieFetcher.getRottenTomatoesMovieList(listType, listName, page);
      switch (listName) {
        case Constants.BOX_OFFICE_PATH:
          EventBus.getDefault().postSticky(new BoxOfficeFetchEvent(movieList.getMovies()));
          break;
        case Constants.OPENING_PATH:
          EventBus.getDefault().postSticky(new OpeningFetchEvent(movieList.getMovies()));
          break;
        case Constants.UPCOMING_PATH:
          if (listType == (MovieListType.MOVIES)) {
            EventBus.getDefault().postSticky(new UpcomingFetchEvent(movieList.getMovies(), movieList.getTotal()));
          } else {
            EventBus.getDefault().postSticky(new ComingSoonFetchEvent(movieList.getMovies(), movieList.getTotal()));
          }
          break;
        case Constants.IN_THEATRES_PATH:
          EventBus.getDefault().postSticky(new InTheatresFetchEvent(movieList.getMovies(), movieList.getTotal()));
          break;
        case Constants.TOP_RENTALS_PATH:
          EventBus.getDefault().postSticky(new TopRentalsFetchEvent(movieList.getMovies()));
          break;
        case Constants.CURRENT_RELEASES_PATH:
          EventBus.getDefault().postSticky(new CurrentReleasesFetchEvent(movieList.getMovies(), movieList.getTotal()));
          break;
        case Constants.NEW_RELEASES_PATH:
          EventBus.getDefault().postSticky(new NewReleasesFetchEvent(movieList.getMovies(), movieList.getTotal()));
          break;
      }
    } catch (Exception e) {
      EventBus.getDefault().postSticky(new FetchErrorEvent(listName));
      Log.e(TAG, "Error Fetching RT Movies - " + listName + ": " + e.getMessage());
      //Crashlytics.logException(e);
    }
  }

  @ServiceAction
  protected void searchMovies(String query) {
    try {
      MovieList movieList = movieFetcher.searchRottenTomatoesMovies(query);
      EventBus.getDefault().postSticky(new SearchResultsFetchEvent(movieList.getMovies()));
    } catch (Exception e) {
      EventBus.getDefault().postSticky(new SearchErrorEvent());
      Log.e(TAG, "Error Searching Movies: " + e.toString());
    }
  }

  @ServiceAction
  protected void getReviews(String movieId) {
    try {
      Reviews reviews = movieFetcher.getReviews(movieId);
      EventBus.getDefault().postSticky(new ReviewsFetchEvent(reviews));
    } catch (Exception e) {
      EventBus.getDefault().postSticky(new ReviewsErrorEvent());
      Log.e(TAG, "Error Getting Reviews: " + e.toString());
    }
  }

  @ServiceAction
  protected void getCredits(String movieId) {
    try {
      Credits credits = movieFetcher.getCredits(movieId);
      EventBus.getDefault().postSticky(new CreditsFetchEvent(credits));
    } catch (Exception e) {
      EventBus.getDefault().postSticky(new CreditsErrorEvent());
      Log.e(TAG, "Error Getting Credits: " + e.toString());
    }
  }

  @ServiceAction
  protected void getTmdbMovie(String id) {
    try {
      TmdbMovie movie = movieFetcher.getTmbdMovie(id);
      EventBus.getDefault().postSticky(new TmdbGetMovieEvent(movie));
    } catch (Exception e) {
      Log.e(TAG, "Error Getting TMDB Movie: " + e.toString());
    }
  }

  @ServiceAction
  protected void findTmdbMovie(String id) {
    try {
      String imdbId = movieInfoParser.getImdbId(id);
      TmdbMovieResult movie = movieFetcher.findTmbdMovie(imdbId);
      EventBus.getDefault().postSticky(new TmdbFindMovieEvent(movie));
    } catch (Exception e) {
      Log.e(TAG, "Error Finding TMDB Movie: " + e.toString());
    }
  }

  @ServiceAction
  protected void getShowtimes(String movieTitle, String coordinates) {
    try {
      RequestQueue queue = Volley.newRequestQueue(this);
      //String url ="http://www.google.com/movies?near=53.343205,%20-6.434563&hl=en&q=cinderella";
      String url = String.format(getString(R.string.showtimes_url), coordinates, URLEncoder.encode(movieTitle, "UTF-8"));

      StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          Log.d(TAG, "response = " + response);
          EventBus.getDefault().postSticky(new ShowtimesFetchEvent(response));
        }
      }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
          EventBus.getDefault().postSticky(new ShowtimesErrorEvent());
          Log.e(TAG, "Error getting showtimes: " + error.toString());
        }
      });
      queue.add(stringRequest);
    } catch (Exception e) {
      EventBus.getDefault().postSticky(new ShowtimesErrorEvent());
      Log.e(TAG, "Error getting showtimes: " + e.toString());
    }
  }

  @ServiceAction
  protected void getGoogleAddress(String coordinates) {
    try {
      Address addressResult = movieFetcher.getGoogleAddress(coordinates);
      EventBus.getDefault().postSticky(new GoogleAddressFetchEvent(addressResult));
    } catch (Exception e) {
      Log.e(TAG, "Error Reverse Geocoding address: " + e.toString());
    }
  }
}
