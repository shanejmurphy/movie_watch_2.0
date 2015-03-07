package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.movie.watch.R;
import com.movie.watch.busevents.OpeningFetchEvent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.movie_selection_fragment)
public class OpeningFragment extends MovieSelectionFragment {

  @ViewById
  protected ProgressBar movieSelectionProgressBar;
  @ViewById
  protected ListView movieSelectionList;

  @FragmentArg
  protected String moviePath;

  public static Fragment create(String moviePath) {
    return OpeningFragment_.builder().moviePath(moviePath).build();
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

  public void onEventMainThread(OpeningFetchEvent openingFetchEvent) {
    EventBus.getDefault().removeStickyEvent(openingFetchEvent);
    movieListAdapter.setMovies(openingFetchEvent.getMovies());
    movieListAdapter.notifyDataSetChanged();
    movieSelectionList.setAdapter(movieListAdapter);
    movieSelectionProgressBar.setVisibility(View.GONE);
    movieSelectionList.setVisibility(View.VISIBLE);
  }
}
