package com.movie.watch.rest;

import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieList;
import com.movie.watch.model.TmdbFindResults;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.address.Address;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RestMovies {
  @GET("/lists/{listType}/{listName}?limit=" + Constants.MAX_RESULTS_PER_PAGE + "&apikey=" + Constants.RT_API_KEY)
  MovieList getRottenTomatoesMovies(@Path("listType") String listType, @Path("listName") String listName,
                                    @Query("page") int page, @Query("country") String countryCode);//, Callback<List<Movie>> callback);

  @GET("/movies.json?page_limit=" + Constants.MAX_RESULTS_PER_PAGE + "&page=1&apikey=" + Constants.RT_API_KEY)
  MovieList searchRottenTomatoesMovies(@Query("q") String query);

  @GET("/movies/{movieId}/reviews.json?apikey=" + Constants.RT_API_KEY)
  Reviews getReviews(@Path("movieId") String movieId);

  @GET("/movie/{id}?api_key=187805121faeae75c16844146e69f2d1&append_to_response=trailers,releases")
  TmdbMovie getTmdbMovie(@Path("id") String id);

  @GET("/find/{id}?api_key=187805121faeae75c16844146e69f2d1&external_source=imdb_id")
  TmdbFindResults findTmdbMovie(@Path("id") String id);

  @GET("/movie/{id}/credits?api_key=187805121faeae75c16844146e69f2d1")
  Credits getCredits(@Path("id") String id);

  @GET("/geocode/json")
  Address getGoogleAddress(@Query("address") String coordinates);

  //http://www.google.com/movies?near=53.343205,%20-6.434563&hl=en&q=cinderella
  @GET("/movies")
  void getShowtimesBody(@Query("q") String movieTitle, @Query("near") String latLng, @Query("hl") String locale, Callback<String> callback);
}
