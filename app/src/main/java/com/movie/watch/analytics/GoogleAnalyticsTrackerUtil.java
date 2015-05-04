package com.movie.watch.analytics;

import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class GoogleAnalyticsTrackerUtil {

  @Bean
  protected GoogleAnalyticsTracker gaTracker;

  //Track Screens
  public void trackScreen(int screenName) {
    gaTracker.trackScreen(screenName);
  }

  public void trackMovieSelectedEvent(String listName) {
    int label;
    switch (listName) {
      case Constants.BOX_OFFICE_PATH:
        label = R.string.top_box_office;
        break;
      case Constants.IN_THEATRES_PATH:
        label = R.string.in_theatres;
        break;
      case Constants.OPENING_PATH:
        label = R.string.opening;
        break;
      case Constants.UPCOMING_PATH:
        label = R.string.upcoming;
        break;
      case Constants.TOP_RENTALS_PATH:
        label = R.string.top_rentals;
        break;
      case Constants.CURRENT_RELEASES_PATH:
        label = R.string.current_releases;
        break;
      default:
      case Constants.NEW_RELEASES_PATH:
        label = R.string.new_releases;
    }
    gaTracker.trackEvent(R.string.ga_event_movie_selection, label);
  }

  public void trackMovieTypeSelectionEvent(int listTypeId) {
    gaTracker.trackEvent(R.string.ga_event_movie_list_type_selection, listTypeId);
  }

  public void trackTrailerPlayEvent(int playButtonId) {
    gaTracker.trackEvent(R.string.ga_event_play_trailer, playButtonId);
  }

  public void trackReviewSelectionEvent() {
    gaTracker.trackEvent(R.string.ga_event_review_selected, R.string.ga_label_review_selected);
  }

  public void trackMovieTypeScreen(MovieListType movieListType) {
    int screenName;
    switch (movieListType) {
      case MOVIES:
        screenName = R.string.title_theaters;
        break;
      default:
      case DVDS:
        screenName = R.string.title_dvds;
    }
    gaTracker.trackScreen(screenName);
  }
}
