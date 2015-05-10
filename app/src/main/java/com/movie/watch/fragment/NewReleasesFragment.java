package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.NewReleasesFetchEvent;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_selection)
public class NewReleasesFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return NewReleasesFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(NewReleasesFetchEvent newReleasesFetchEvent) {
    EventBus.getDefault().removeStickyEvent(newReleasesFetchEvent);
    movies = (ArrayList) newReleasesFetchEvent.getMovies();
    totalMovies = newReleasesFetchEvent.getTotal();
    displayMovieList(totalMovies);
  }
}
