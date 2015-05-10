package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.InTheatresFetchEvent;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_selection)
public class InTheatresFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return InTheatresFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(InTheatresFetchEvent inTheatresFetchEvent) {
    EventBus.getDefault().removeStickyEvent(inTheatresFetchEvent);
    displayMovies(inTheatresFetchEvent.getMovies(), inTheatresFetchEvent.getTotal());
  }
}
