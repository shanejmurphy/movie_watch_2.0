package com.movie.watch.network;

import com.movie.watch.model.MovieList;

import retrofit.http.GET;
import retrofit.http.Path;

public interface RottenTomatoesService {
  @GET("/api/public/v1.0/lists/movies/{listType}?limit=16&country=us&apikey=wtcku8jhpcj3zh6gtexnb94x")
  MovieList getRottenTomatoesMovies(@Path("listType") String listType);//, Callback<List<Movie>> callback);
}
