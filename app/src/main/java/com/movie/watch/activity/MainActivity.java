package com.movie.watch.activity;

import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.busevents.MovieFetchEvent;
import com.movie.watch.constants.Constants;
import com.movie.watch.fragment.MovieSelectionFragment;
import com.movie.watch.model.Movie;
import com.movie.watch.service.MovieFetchingService_;
import com.movie.watch.utils.MovieFetcher;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  @Bean
  protected MovieFetcher movieFetcher;

  @ViewById
  protected ProgressBar mainProgressBar;
  @ViewById
  protected AdView adView;

  @AfterViews
  protected void afterViews() {
    loadAds(adView);
    setLollipopStatusBarColor();
    getMovies(Constants.BOX_OFFICE_PATH);
  }

  @Background
  protected void getMovies(String listType) {
    MovieFetchingService_.intent(this).fetchMovies(listType).start();
  }

/*  @Background
  protected void getTMDBMovies(String listType) {
    TmdbMovies movies = new TmdbApi(Constants.TMDB_API_KEY).getMovies();
  }*/

  private void createMovieSelectionFragment(ArrayList<Movie> movies) {
    MovieSelectionFragment movieSelectionFragment = (MovieSelectionFragment) MovieSelectionFragment.create(movies);
    replaceFragment(R.id.selectionPane, movieSelectionFragment, null);
  }

  public void onEventMainThread(MovieFetchEvent movieFetchEvent) {
    EventBus.getDefault().removeStickyEvent(movieFetchEvent);
    mainProgressBar.setVisibility(View.GONE);
    ArrayList<Movie> movies = new ArrayList(movieFetchEvent.getMovies());
    createMovieSelectionFragment(movies);
  }
}
