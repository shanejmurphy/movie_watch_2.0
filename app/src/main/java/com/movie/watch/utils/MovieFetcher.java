package com.movie.watch.utils;

import android.util.Log;

import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.model.TmdbFindResults;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;
import com.movie.watch.rest.DefaultCallback;
import com.movie.watch.rest.RestMovies;

import org.androidannotations.annotations.EBean;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

@EBean
public class MovieFetcher {
  private static final String TAG = MovieFetcher.class.getSimpleName();

  public MovieList getRottenTomatoesMovieList(String movieListType) {
    RestAdapter restAdapter = buildRestAdapter(Constants.ROTTEN_TOMATOES_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    MovieList movieList = service.getRottenTomatoesMovies(movieListType);
    return movieList;//, new MovieResponseCallback());
  }

  public Reviews getReviews(String movieId) {
    RestAdapter restAdapter = buildRestAdapter(Constants.ROTTEN_TOMATOES_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    Reviews reviews = service.getReviews(movieId);
    return reviews;
  }

  public TmdbMovie getTmbdMovie(String id) {
    RestAdapter restAdapter = buildRestAdapter(Constants.TMDB_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    TmdbMovie movie = service.getTmdbMovie(id);
    return movie;
  }

  public TmdbMovieResult findTmbdMovie(String imdbId) {
    RestAdapter restAdapter = buildRestAdapter(Constants.TMDB_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    TmdbFindResults tmdbResults = service.findTmdbMovie(imdbId);
    List<TmdbMovieResult> movieResults = tmdbResults.getMovieResults();
    return movieResults == null || movieResults.isEmpty() ? null : movieResults.get(0);
  }

  public Credits getCredits(String movieId) {
    RestAdapter restAdapter = buildRestAdapter(Constants.TMDB_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    Credits credits = service.getCredits(movieId);
    return credits;
  }

  private RestAdapter buildRestAdapter(String url) {
    return new RestAdapter.Builder()
    .setEndpoint(url)
    .setLogLevel(RestAdapter.LogLevel.FULL)
    .build();
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
