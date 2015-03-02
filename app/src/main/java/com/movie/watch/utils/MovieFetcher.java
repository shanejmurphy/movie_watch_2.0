package com.movie.watch.utils;

import android.util.Log;

import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.network.RottenTomatoesService;
import com.movie.watch.rest.DefaultCallback;

import org.androidannotations.annotations.EBean;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

@EBean
public class MovieFetcher {
  private static final String TAG = MovieFetcher.class.getSimpleName();

  public MovieList getRottenTomatoesMovieList(String movieListType) {
    RestAdapter restAdapter = new RestAdapter.Builder()
    .setEndpoint(Constants.BASE_URL)
    .setLogLevel(RestAdapter.LogLevel.FULL)
    .build();

    RottenTomatoesService service = restAdapter.create(RottenTomatoesService.class);

    MovieList movieList = service.getBoxOfficeMovies(movieListType);
    return movieList;//, new MovieResponseCallback());
  }

  private class MovieResponseCallback extends DefaultCallback<MovieList> {
    @Override
    public void onSuccess(MovieList movies) {
      if(!movies.getMovies().isEmpty()) {
        Log.d(TAG, "movies.size() = " + movies.getMovies().size());
      }
    }

    @Override
    protected void onFailure(RetrofitError error) {
      Log.d(TAG, error.getResponse().getBody().toString());
    }
  }
}
