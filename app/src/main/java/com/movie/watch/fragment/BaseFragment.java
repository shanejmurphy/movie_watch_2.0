package com.movie.watch.fragment;

import android.support.v4.app.Fragment;

import com.movie.watch.model.MovieListType;
import com.movie.watch.service.MovieFetchingService_;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment
public class BaseFragment extends Fragment {

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

  @Background
  protected void getMovies(MovieListType listType, String listName, int page) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).fetchMovies(listType, listName, page).start();
  }

  @Background
  protected void getReviews(String movieId) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).getReviews(movieId).start();
  }

  @Background
  protected void getCredits(String movieId) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).getCredits(movieId).start();
  }

  @Background
  protected void getTmdbMovie(String id) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).getTmdbMovie(id).start();
  }

  @Background
  protected void findTmdbMovie(String id) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).findTmdbMovie(id).start();
  }

  @Background
  protected void searchMovies(String query) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).searchMovies(query).start();
  }

  @Background
  protected void getShowtimes(String movieTitle, String coordinates) {
    MovieFetchingService_.intent(getActivity().getApplicationContext()).getShowtimes(movieTitle, coordinates).start();
  }
}
