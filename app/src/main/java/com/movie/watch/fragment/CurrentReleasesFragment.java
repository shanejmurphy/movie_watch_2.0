package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.CurrentReleasesFetchEvent;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.movie_selection_fragment)
public class CurrentReleasesFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return CurrentReleasesFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(CurrentReleasesFetchEvent currentReleasesFetchEvent) {
    EventBus.getDefault().removeStickyEvent(currentReleasesFetchEvent);
    displayMovies(currentReleasesFetchEvent.getMovies(), currentReleasesFetchEvent.getTotal());
  }
}
