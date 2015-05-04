package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.R;
import com.movie.watch.busevents.BoxOfficeFetchEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.movie_selection_fragment)
public class BoxOfficeFragment extends MovieResultsFragment {

  public static Fragment create(MovieListType listType, String listName) {
    return BoxOfficeFragment_.builder().listType(listType).listName(listName).build();
  }

  public void onEventMainThread(BoxOfficeFetchEvent boxOfficeFetchEvent) {
    EventBus.getDefault().removeStickyEvent(boxOfficeFetchEvent);
    displayMovies(boxOfficeFetchEvent.getMovies(), Constants.MAX_RESULTS_PER_PAGE);
  }
}
