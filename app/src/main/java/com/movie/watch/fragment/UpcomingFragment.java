package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.UpcomingFetchEvent;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_selection)
public class UpcomingFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return UpcomingFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(UpcomingFetchEvent upcomingFetchEvent) {
    EventBus.getDefault().removeStickyEvent(upcomingFetchEvent);
    displayMovies(upcomingFetchEvent.getMovies(), upcomingFetchEvent.getTotal());
  }
}
