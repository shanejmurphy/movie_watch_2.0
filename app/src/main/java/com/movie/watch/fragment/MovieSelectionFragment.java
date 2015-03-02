package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
  protected Toolbar mainToolbar;
  @ViewById
  protected ListView movieSelectionList;

  @Bean
  protected MovieListAdapter movieListAdapter;

  @FragmentArg
  protected ArrayList<Movie> movies;

  public static Fragment create(ArrayList<Movie> movies) {
    return MovieSelectionFragment_.builder().movies(movies).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    mainToolbar.setTitle(getString(R.string.app_name));
    movieListAdapter.setMovies(movies);
    movieSelectionList.setAdapter(movieListAdapter);
  }
}
