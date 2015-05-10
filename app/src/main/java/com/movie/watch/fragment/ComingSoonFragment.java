package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.ComingSoonFetchEvent;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_selection)
public class ComingSoonFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return ComingSoonFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(ComingSoonFetchEvent comingSoonFetchEvent) {
    EventBus.getDefault().removeStickyEvent(comingSoonFetchEvent);
    displayMovies(comingSoonFetchEvent.getMovies(), comingSoonFetchEvent.getTotal());
  }
}
