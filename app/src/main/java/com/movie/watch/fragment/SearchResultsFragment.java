package com.movie.watch.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.movie.watch.R;
import com.movie.watch.activity.MovieContentActivity_;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.SearchErrorEvent;
import com.movie.watch.busevents.SearchResultsFetchEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.Movie;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.movie_selection_fragment)
public class SearchResultsFragment extends MovieSelectionFragment {

  @FragmentArg
  protected String query;

  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  public static Fragment create(String query) {
    return SearchResultsFragment_.builder().query(query).build();
  }

  @AfterInject
  public void afterInject() {
    if (movies == null) {
      gaTrackerUtil.trackScreen(R.string.title_search_results);
    }
  }

  @AfterViews
  public void afterViews() {
    if (movies == null) {
      searchMovies(query);
      movieSelectionProgressBar.setVisibility(View.VISIBLE);
      movieSelectionList.setVisibility(View.GONE);
    } else {
      displayMovieList(Constants.MAX_RESULTS_PER_PAGE);
    }
  }

  @ItemClick(R.id.movieSelectionList)
  public void movieListItemClicked(Movie movie) {
    findTmdbMovie(movie.getAlternateIds());
    getReviews(movie.getId());
    MovieContentActivity_.intent(this).movie(movie).start();
  }

  public void onEventMainThread(SearchResultsFetchEvent searchResultsFetchEvent) {
    EventBus.getDefault().removeStickyEvent(searchResultsFetchEvent);
    movies = (ArrayList) searchResultsFetchEvent.getMovies();
    displayMovieList(Constants.MAX_RESULTS_PER_PAGE);
  }

  public void onEventMainThread(SearchErrorEvent searchErrorEvent) {
    EventBus.getDefault().removeStickyEvent(searchErrorEvent);
    movieSelectionProgressBar.setVisibility(View.GONE);
    movieSelectionErrorText.setVisibility(View.VISIBLE);
  }
}
