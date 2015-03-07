package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movie.watch.adapter.MovieListAdapter;
import com.movie.watch.service.MovieFetchingService_;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment
public class MovieSelectionFragment extends Fragment {

  @Bean
  protected MovieListAdapter movieListAdapter;

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @Background
  protected void getMovies(String listType) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).fetchMovies(listType).start();
  }

/*  public void onEventMainThread(UpcomingMoviesFetchEvent upcomingMoviesFetchEvent) {
    //movieSelectionProgressBar.setVisibility(View.GONE);
    //movieSelectionList.setVisibility(View.VISIBLE);
    EventBus.getDefault().removeStickyEvent(upcomingMoviesFetchEvent);
    movieListAdapter.setMovies(upcomingMoviesFetchEvent.getMovies());
    movieListAdapter.notifyDataSetChanged();
    movieSelectionList.setAdapter(movieListAdapter);
  }*/

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().registerSticky(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }
}
