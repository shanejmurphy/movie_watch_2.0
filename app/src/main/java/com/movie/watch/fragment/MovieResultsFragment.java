package com.movie.watch.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movie.watch.R;
import com.movie.watch.activity.MovieContentActivity_;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.FetchErrorEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.Movie;
import com.movie.watch.utils.LocationHelper;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EFragment
public class MovieResultsFragment extends MovieSelectionFragment {
  private static final String TAG = MovieResultsFragment.class.getSimpleName();

  @Bean
  protected MovieInfoParser movieInfoParser;
  @Bean
  protected LocationHelper locationHelper;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @InstanceState
  protected int totalMovies;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterInject
  public void afterInject() {
    if (movies == null) {
      gaTrackerUtil.trackMovieTypeScreen(listType);
    }
  }

  @AfterViews
  public void afterViews() {
    if (movies == null) {
      getMovies(listType, listName, 1);
      movieSelectionProgressBar.setVisibility(View.VISIBLE);
      movieSelectionList.setVisibility(View.GONE);
    } else {
      displayMovieList(totalMovies);
    }
  }

  @ItemClick(R.id.movieSelectionList)
  public void movieListItemClicked(Movie movie) {
    findTmdbMovie(movie.getAlternateIds());
    getReviews(movie.getId());
    //do we need to display shpwtimes?
    String theaterReleaseDate = movie.getReleaseDates().getTheater();
    String coordinates = locationHelper.getLocationCoordinates();
    if(movieInfoParser.isShowing(theaterReleaseDate) && !TextUtils.isEmpty(coordinates)) {
      getShowtimes(movie.getTitle(), coordinates);
    }
    MovieContentActivity_.intent(this).movie(movie).start();
    gaTrackerUtil.trackMovieSelectedEvent(listName);
  }

  protected void displayMovies(List<Movie> fetchedMovies, int totalFetchableMovies) {
    if (movies == null) {
      movies = new ArrayList<>();
    }

    int index = (pageNum - 1) * Constants.MAX_RESULTS_PER_PAGE;
    movies.addAll(index, fetchedMovies);
    if (pageNum == 1) {
      totalMovies = totalFetchableMovies;
      displayMovieList(totalMovies);
    } else {
      updateMovieList();
    }
  }

  public void onEventMainThread(FetchErrorEvent fetchErrorEvent) {
    EventBus.getDefault().removeStickyEvent(fetchErrorEvent);
    if (!TextUtils.isEmpty(fetchErrorEvent.getListName()) && fetchErrorEvent.getListName().equals(listName)) {
      movieSelectionProgressBar.setVisibility(View.GONE);
      movieSelectionErrorText.setVisibility(View.VISIBLE);
    }
  }
}
