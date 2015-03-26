package com.movie.watch.busevents;

import com.movie.watch.model.TmdbMovie;

//get movie from tmdb using rt id
public class TmdbGetMovieEvent {
  private TmdbMovie movie;

  public TmdbGetMovieEvent(TmdbMovie movie) {
    this.movie = movie;
  }

  public TmdbMovie getMovie() {
    return movie;
  }

}
