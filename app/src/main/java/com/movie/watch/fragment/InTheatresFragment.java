package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movie.watch.R;
import com.movie.watch.busevents.InTheatresFetchEvent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.movie_selection_fragment)
public class InTheatresFragment extends MovieSelectionFragment {

  public static Fragment create(String moviePath) {
    return InTheatresFragment_.builder().moviePath(moviePath).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterInject
  public void afterInject() {
    getMovies(moviePath);
  }

  @AfterViews
  public void afterViews() {
    movieSelectionProgressBar.setVisibility(View.VISIBLE);
    movieSelectionList.setVisibility(View.GONE);
  }

  public void onEventMainThread(InTheatresFetchEvent inTheatresFetchEvent) {
    EventBus.getDefault().removeStickyEvent(inTheatresFetchEvent);
    movieListAdapter.setMovies(inTheatresFetchEvent.getMovies());
    movieListAdapter.notifyDataSetChanged();
    movieSelectionList.setAdapter(movieListAdapter);
    movieSelectionProgressBar.setVisibility(View.GONE);
    movieSelectionList.setVisibility(View.VISIBLE);
  }
}
