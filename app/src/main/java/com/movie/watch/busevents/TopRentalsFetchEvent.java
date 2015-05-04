package com.movie.watch.busevents;

import com.movie.watch.model.Movie;

import java.util.List;

public class TopRentalsFetchEvent {
    private List<Movie> movies;

    public TopRentalsFetchEvent(List<Movie> movies) {
      this.movies = movies;
    }

    public List<Movie> getMovies() {
      return movies;
    }

}
