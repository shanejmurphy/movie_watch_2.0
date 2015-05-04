package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class SearchResultsFetchEvent {
    private List<Movie> movies;

    public SearchResultsFetchEvent(List<Movie> movies) {
      this.movies = movies;
    }

    public List<Movie> getMovies() {
      return movies;
    }

}
