package com.movie.watch.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.model.MovieListType;
import com.movie.watch.model.TmdbFindResults;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.TmdbMovieResult;
import com.movie.watch.model.address.Address;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;
import com.movie.watch.rest.RestMovies;
import com.movie.watch.utils.typeadapter.IntegerTypeAdapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@EBean
public class MovieFetcher {
  private static final String TAG = MovieFetcher.class.getSimpleName();

  @RootContext
  Context context;

  @Bean
  protected LocationHelper locationHelper;

  public MovieList getRottenTomatoesMovieList(MovieListType listType, String listName, int page) {
    RestAdapter restAdapter = buildRestAdapter(Constants.ROTTEN_TOMATOES_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    String listTypeStr = listType.name().toLowerCase();
    return service.getRottenTomatoesMovies(listTypeStr, listName, page, locationHelper.getCountryCode());
  }

  public MovieList searchRottenTomatoesMovies(String query) {
    RestAdapter restAdapter = buildRestAdapter(Constants.ROTTEN_TOMATOES_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    return service.searchRottenTomatoesMovies(query);
  }

  public Reviews getReviews(String movieId) {
    RestAdapter restAdapter = buildRestAdapter(Constants.ROTTEN_TOMATOES_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    return service.getReviews(movieId);
  }

  public TmdbMovie getTmbdMovie(String id) {
    RestAdapter restAdapter = buildRestAdapter(Constants.TMDB_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    return service.getTmdbMovie(id);
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

    return service.getCredits(movieId);
  }

  public Address getGoogleAddress(String coordinates) {
    RestAdapter restAdapter = buildRestAdapter(Constants.GOOGLE_APIS_BASE_URL);
    RestMovies service = restAdapter.create(RestMovies.class);

    Address addresses = service.getGoogleAddress(coordinates);
    return addresses;
  }


  private RestAdapter buildRestAdapter(String url) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
    Gson gson = gsonBuilder.create();

    return new RestAdapter.Builder()
    .setEndpoint(url)
    .setConverter(new GsonConverter(gson))
    .setLogLevel(RestAdapter.LogLevel.FULL)
    .build();
  }
}
