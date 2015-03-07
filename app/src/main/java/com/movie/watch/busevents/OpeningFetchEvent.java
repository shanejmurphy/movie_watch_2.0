package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class OpeningFetchEvent {
    private List<Movie> movies;

    public OpeningFetchEvent(List<Movie> movies) {
      this.movies = movies;
    }

    public List<Movie> getMovies() {
      return movies;
    }

}
