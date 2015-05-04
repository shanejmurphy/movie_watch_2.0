package com.movie.watch.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.AlternateIds;
import com.movie.watch.model.Movie;
import com.movie.watch.model.MovieListType;
import com.movie.watch.utils.EndlessScrollListener;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment
public class MovieSelectionFragment extends BaseFragment {
  private static final String TAG = MovieSelectionFragment.class.getSimpleName();

  @ViewById
  protected ProgressBar movieSelectionProgressBar;
  @ViewById
  protected TextView movieSelectionErrorText;
  @ViewById
  protected ListView movieSelectionList;

  @Bean
  protected MovieListAdapter movieListAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;

  @FragmentArg
  protected MovieListType listType;
  @FragmentArg
  protected String listName;

  @InstanceState
  ArrayList<Movie> movies;
  @InstanceState
  int pageNum = 1;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  protected void displayMovieList(int totalMovies) {
    movieListAdapter.setMovies(movies);
    movieListAdapter.notifyDataSetChanged();
    movieSelectionList.setAdapter(movieListAdapter);
    if (totalMovies > Constants.MAX_RESULTS_PER_PAGE) {
      setUpScrollListener(totalMovies);
    }
    movieSelectionProgressBar.setVisibility(View.GONE);
    movieSelectionList.setVisibility(View.VISIBLE);
  }

  protected void updateMovieList() {
    movieListAdapter.notifyDataSetChanged();
  }

  private void setUpScrollListener(final int totalMovies) {
    movieSelectionList.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        if((pageNum * Constants.MAX_RESULTS_PER_PAGE) < totalMovies) {
          pageNum++;
          getMovies(listType, listName, pageNum);
          Log.i(TAG, "Loading more movies");
        }
      }
    });
  }

  protected void findTmdbMovie(AlternateIds alternateIds) {
    if (alternateIds != null && alternateIds.getImdb() != null) {
      findTmdbMovie(alternateIds.getImdb());
    }
  }
}
