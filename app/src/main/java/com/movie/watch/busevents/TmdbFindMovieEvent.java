package com.movie.watch.busevents;

import com.movie.watch.model.TmdbMovieResult;

//find movie from tmdb using imdb id
public class TmdbFindMovieEvent {
  private TmdbMovieResult movie;

  public TmdbFindMovieEvent(TmdbMovieResult movie) {
    this.movie = movie;
  }

  public TmdbMovieResult getMovie() {
    return movie;
  }

}
