package com.movie.watch.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.fragment.MovieContentFragment;
import com.movie.watch.model.Movie;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_movie_content)
public class MovieContentActivity extends BaseActivity {
  private static final String TAG = MovieContentActivity.class.getSimpleName();
  public static final String FRAGMENT_TAG = "movieContentFragmentContainer";

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected AdView adView;

  @Extra
  protected Movie movie;

  @AfterViews
  protected void afterViews() {
    setUpToolbar();
    //loadAds(adView);
    setLollipopStatusBarColor();
    loadFragment();
  }

  private void setUpToolbar() {
    mainToolbar.setTitle(movie.getTitle());
    setSupportActionBar(mainToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
  }

  private void loadFragment() {
    Fragment fragment = MovieContentFragment.create(movie);
    getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, fragment, FRAGMENT_TAG).commit();
  }

  @OptionsItem(android.R.id.home)
  protected void goUp() {
    finish();
  }
}
