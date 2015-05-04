package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class InTheatresFetchEvent {
  private List<Movie> movies;
  private Integer total;

  public InTheatresFetchEvent(List<Movie> movies, Integer totalMovies) {
    this.movies = movies;
    this.total = totalMovies;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public Integer getTotal() {
    return total;
  }
}
