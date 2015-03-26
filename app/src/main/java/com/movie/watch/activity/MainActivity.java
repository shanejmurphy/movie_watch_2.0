package com.movie.watch.activity;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.fragment.MainFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  public static final String FRAGMENT_TAG = "mainFragmentContainer";

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected AdView adView;

  @AfterViews
  protected void afterViews() {
    mainToolbar.setTitle(getString(R.string.app_name));
    //loadAds(adView);
    setLollipopStatusBarColor();
    loadFragment();
  }

  private void loadFragment() {
    Fragment mainFragment = MainFragment.create();
    getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, mainFragment).commit();
    //replaceFragment(R.id.mainFragmentContainer, mainFragment, FRAGMENT_TAG);
  }
}
