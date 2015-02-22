package com.movie.watch.activity;

import com.google.android.gms.ads.AdView;
import com.movie.watch.Movie;
import com.movie.watch.R;
import com.movie.watch.fragment.MovieSelectionFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseActivity {

  @ViewById
  protected AdView adView;

  private MovieSelectionFragment movieSelectionFragment;
  private ArrayList<Movie> movies;

  @AfterViews
  protected void afterViews() {
    loadAds(adView);
    setLollipopStatusBarColor();
    ArrayList<Movie> movies = createTempMovieList();
    createMovieSelectionFragment(movies);
  }

  private void createMovieSelectionFragment(ArrayList<Movie> movies) {
    movieSelectionFragment = (MovieSelectionFragment) MovieSelectionFragment.create(movies);
    replaceFragment(R.id.selectionPane, movieSelectionFragment, null);
  }

  private ArrayList<Movie> createTempMovieList() {
    Movie movie = new Movie();
    movie.setTitle("Movie Title");
    movie.setProfileImg("http://content7.flixster.com/movie/11/18/90/11189077_tmb.jpg");
    ArrayList<Movie> movies = new ArrayList<>();
    movies.add(movie);
    return movies;
  }
}
