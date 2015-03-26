package com.movie.watch.rest;

import com.movie.watch.model.MovieList;
import com.movie.watch.model.TmdbFindResults;
import com.movie.watch.model.TmdbMovie;
import com.movie.watch.model.credits.Credits;
import com.movie.watch.model.reviews.Reviews;

import retrofit.http.GET;
import retrofit.http.Path;

public interface RestMovies {
  @GET("/api/public/v1.0/lists/movies/{listType}?limit=16&country=us&apikey=wtcku8jhpcj3zh6gtexnb94x")
  MovieList getRottenTomatoesMovies(@Path("listType") String listType);//, Callback<List<Movie>> callback);

  @GET("/api/public/v1.0/movies/{movieId}/reviews.json?apikey=wtcku8jhpcj3zh6gtexnb94x")
  Reviews getReviews(@Path("movieId") String movieId);

  @GET("/movie/{id}?api_key=187805121faeae75c16844146e69f2d1&append_to_response=trailers")
  TmdbMovie getTmdbMovie(@Path("id") String id);

  @GET("/find/{id}?api_key=187805121faeae75c16844146e69f2d1&external_source=imdb_id")
  TmdbFindResults findTmdbMovie(@Path("id") String id);

  @GET("/movie/{id}/credits?api_key=187805121faeae75c16844146e69f2d1")
  Credits getCredits(@Path("id") String id);
}
