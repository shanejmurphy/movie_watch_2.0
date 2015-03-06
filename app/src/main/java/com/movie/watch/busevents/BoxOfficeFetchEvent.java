package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class BoxOfficeFetchEvent {
    private List<Movie> movies;

    public BoxOfficeFetchEvent(List<Movie> movies) {
      this.movies = movies;
    }

    public List<Movie> getMovies() {
      return movies;
    }

}
