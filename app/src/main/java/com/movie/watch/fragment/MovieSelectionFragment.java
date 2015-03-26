package com.movie.watch.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.movie.watch.R;
import com.movie.watch.activity.MovieContentActivity_;
import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.model.AlternateIds;
import com.movie.watch.model.Movie;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EFragment
public class MovieSelectionFragment extends BaseFragment {
  private static final String TAG = MovieSelectionFragment.class.getSimpleName();

  @ViewById
  protected ProgressBar movieSelectionProgressBar;
  @ViewById
  protected ListView movieSelectionList;

  @Bean
  protected MovieListAdapter movieListAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;

  @FragmentArg
  protected String moviePath;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @ItemClick(R.id.movieSelectionList)
  public void movieListItemClicked(Movie movie) {
    findTmdbMovie(movie.getAlternateIds());
    getReviews(movie.getId());
    MovieContentActivity_.intent(this).movie(movie).start();
  }

  private void findTmdbMovie(AlternateIds alternateIds) {
    if (alternateIds != null && alternateIds.getImdb() != null) {
      findTmdbMovie(alternateIds.getImdb());
    }
  }
}
