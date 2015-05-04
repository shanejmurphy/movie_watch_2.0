package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class NewReleasesFetchEvent {
  private List<Movie> movies;
  private Integer total;

  public NewReleasesFetchEvent(List<Movie> movies, Integer total) {
    this.movies = movies;
    this.total = total;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public Integer getTotal() {
    return total;
  }
}
