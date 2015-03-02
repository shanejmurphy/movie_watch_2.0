package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class MovieFetchEvent {
    private List<Movie> movies;

    public MovieFetchEvent(List<Movie> movies) {
      this.movies = movies;
    }

    public List<Movie> getMovies() {
      return movies;
    }

}
