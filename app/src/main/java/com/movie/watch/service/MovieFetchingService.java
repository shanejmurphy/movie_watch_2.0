package com.movie.watch.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.movie.watch.busevents.BoxOfficeFetchEvent;
import com.movie.watch.busevents.CreditsFetchEvent;
import com.movie.watch.busevents.InTheatresFetchEvent;
import com.movie.watch.busevents.OpeningFetchEvent;
import com.movie.watch.busevents.ReviewsFetchEvent;
import com.movie.watch.busevents.TmdbFindMovieEvent;
import com.movie.watch.busevents.TmdbGetMovieEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;
import com.movie.watch.utils.MovieFetcher;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

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

  //TODO - error handling

  @ServiceAction
  protected void fetchMovies(String movieListType) {
    MovieList movieList;
    movieList = movieFetcher.getRottenTomatoesMovieList(movieListType);
    try {
      switch (movieListType) {
        case Constants.BOX_OFFICE_PATH:
          EventBus.getDefault().postSticky(new BoxOfficeFetchEvent(movieList.getMovies()));
          break;
        case Constants.OPENING_PATH:
          EventBus.getDefault().postSticky(new InTheatresFetchEvent(movieList.getMovies()));
          break;
        case Constants.IN_THEATRES_PATH:
          EventBus.getDefault().postSticky(new OpeningFetchEvent(movieList.getMovies()));
      }
    } catch (Exception e) {
      Log.d(TAG, "Error Fetching RT Movies");
    }
  }

  @ServiceAction
  protected void getReviews(String movieId) {
    try {
      Reviews reviews = movieFetcher.getReviews(movieId);
      EventBus.getDefault().postSticky(new ReviewsFetchEvent(reviews));
    } catch (Exception e) {
      Log.d(TAG, "Error Getting Reviews: " + e.toString());
    }
  }

  @ServiceAction
  protected void getCredits(String movieId) {
    try {
      Credits credits = movieFetcher.getCredits(movieId);
      EventBus.getDefault().postSticky(new CreditsFetchEvent(credits));
    } catch (Exception e) {
      Log.d(TAG, "Error Getting Reviews: " + e.toString());
    }
  }

  @ServiceAction
  protected void getTmdbMovie(String id) {
    try {
      TmdbMovie movie = movieFetcher.getTmbdMovie(id);
      EventBus.getDefault().postSticky(new TmdbGetMovieEvent(movie));
    } catch (Exception e) {
      Log.d(TAG, "Error Getting TMDB Movie: " + e.toString());
    }
  }

  @ServiceAction
  protected void findTmdbMovie(String id) {
    try {
      String imdbId = movieInfoParser.getImdbId(id);
      TmdbMovieResult movie = movieFetcher.findTmbdMovie(imdbId);
      EventBus.getDefault().postSticky(new TmdbFindMovieEvent(movie));
    } catch (Exception e) {
      Log.d(TAG, "Error Finding TMDB Movie: " + e.toString());
    }
  }
}
