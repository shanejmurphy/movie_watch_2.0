package com.movie.watch.activity;

import com.movie.watch.R;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.fragment.MovieContentFragment;
import com.movie.watch.model.Movie;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;

@EActivity(R.layout.activity_movie_content)
public class MovieContentActivity extends BaseActivity {
  private static final String TAG = MovieContentActivity.class.getSimpleName();
  public static final String FRAGMENT_TAG = "movieContentFragmentContainer";

  @Extra
  protected Movie movie;

  @InstanceState
  protected boolean isAlreadyLoaded;

  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @AfterInject
  protected void afterInject() {
    gaTrackerUtil.trackScreen(R.string.ga_screen_movie_content);
  }

  @AfterViews
  protected void afterViews() {
    setUpActionBar(movie.getTitle());
    loadAds(adView);
    setLollipopStatusBarColor();
    if (!isAlreadyLoaded) {
      loadFragment(R.id.mainFragmentContainer, MovieContentFragment.create(movie), FRAGMENT_TAG);
      isAlreadyLoaded = true;
    }
  }

  @OptionsItem(android.R.id.home)
  protected void goUp() {
    finish();
  }
}
