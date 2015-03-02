package com.movie.watch.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.movie.watch.busevents.MovieFetchEvent;
import com.movie.watch.model.MovieList;
import com.movie.watch.utils.MovieFetcher;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

import de.greenrobot.event.EventBus;

@EIntentService
public class MovieFetchingService extends IntentService {
  private static final String TAG = MovieFetchingService.class.getSimpleName();

  @Bean
  protected MovieFetcher movieFetcher;

  public MovieFetchingService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //do nothing - not required when using annotations
  }

  @ServiceAction
  protected void fetchMovies(String movieListType) {
    try {
      MovieList movieList = movieFetcher.getRottenTomatoesMovieList(movieListType);
      EventBus.getDefault().postSticky(new MovieFetchEvent(movieList.getMovies()));
    } catch (Exception e) {
      Log.d(TAG, "Error Fetching Movies");
    }
  }
}
