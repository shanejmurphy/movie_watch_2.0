package com.movie.watch.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.busevents.FetchEventEnded;
import com.movie.watch.constants.Constants;
import com.movie.watch.fragment.MovieSelectionFragment;
import com.movie.watch.model.Movie;
import com.movie.watch.utils.MovieFetcher;

import org.androidannotations.annotations.AfterInject;
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

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected ProgressBar mainProgressBar;
  @ViewById
  protected ViewPager movieSelectionPager;
  @ViewById
  protected AdView adView;

  @Bean
  protected MovieFetcher movieFetcher;

  private ArrayList<Movie> boxOffice;
  private ArrayList<Movie> openingSoon;
  private ArrayList<Movie> inTheatres;

  @AfterInject
  protected void afterInject() {
    getMovies();
  }

  @AfterViews
  protected void afterViews() {
    mainToolbar.setTitle(getString(R.string.app_name));
    mainProgressBar.setVisibility(View.VISIBLE);
    //loadAds(adView);
    setLollipopStatusBarColor();
    movieSelectionPager.setOffscreenPageLimit(2);
  }

/*  @Background
  protected void getTMDBMovies(String listType) {
    TmdbMovies movies = new TmdbApi(Constants.TMDB_API_KEY).getMovies();
  }*/

  @Background
  protected void getMovies() {
    boxOffice = new ArrayList<>(movieFetcher.getRottenTomatoesMovieList(Constants.BOX_OFFICE_PATH).getMovies());
    openingSoon = new ArrayList<>(movieFetcher.getRottenTomatoesMovieList(Constants.OPENING_PATH).getMovies());
    inTheatres = new ArrayList<>(movieFetcher.getRottenTomatoesMovieList(Constants.IN_THEATRES_PATH).getMovies());
    EventBus.getDefault().post(new FetchEventEnded());
  }

  public void onEventMainThread(FetchEventEnded fetchEventEnded) {
    mainProgressBar.setVisibility(View.GONE);
    movieSelectionPager.setAdapter(new MoviePagerAdapter(getSupportFragmentManager()));
  }

  private class MoviePagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;

    public MoviePagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return MovieSelectionFragment.create(boxOffice);
        case 1:
          return MovieSelectionFragment.create(openingSoon);
        default:
          return MovieSelectionFragment.create(inTheatres);
      }
    }
  }
}
