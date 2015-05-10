package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.OpeningFetchEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_selection)
public class OpeningFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return OpeningFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(OpeningFetchEvent openingFetchEvent) {
    EventBus.getDefault().removeStickyEvent(openingFetchEvent);
    displayMovies(openingFetchEvent.getMovies(), Constants.MAX_RESULTS_PER_PAGE);
  }
}
