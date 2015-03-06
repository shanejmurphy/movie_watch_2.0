package com.movie.watch.fragment;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.movie.watch.R;
import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.model.Movie;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.movie_selection_fragment)
public class MovieSelectionFragment extends Fragment {

  @ViewById
  protected ListView movieSelectionList;

  @Bean
  protected MovieListAdapter movieListAdapter;

  @FragmentArg
  protected ArrayList<Movie> movies;

  public static Fragment create(ArrayList<Movie> movies) {
    return MovieSelectionFragment_.builder().movies(movies).build();
  }

  @AfterViews
  public void afterViews() {
    movieListAdapter.setMovies(movies);
    movieSelectionList.setAdapter(movieListAdapter);
  }
}
