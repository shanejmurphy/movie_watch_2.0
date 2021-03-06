package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class CurrentReleasesFetchEvent {
  private List<Movie> movies;
  private Integer total;

  public CurrentReleasesFetchEvent(List<Movie> movies, Integer total) {
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
