package com.movie.watch.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.fragment.BoxOfficeFragment;
import com.movie.watch.fragment.InTheatresFragment;
import com.movie.watch.fragment.OpeningFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.main_activity)
public class MainActivity extends BaseActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected PagerSlidingTabStrip tabs;
  @ViewById
  protected ViewPager movieSelectionPager;
  @ViewById
  protected AdView adView;

  @AfterViews
  protected void afterViews() {
    mainToolbar.setTitle(getString(R.string.app_name));
    //loadAds(adView);
    setLollipopStatusBarColor();
    movieSelectionPager.setOffscreenPageLimit(2);
    movieSelectionPager.setAdapter(new MovieListsPagerAdapter(getSupportFragmentManager()));
    tabs.setViewPager(movieSelectionPager);
  }

/*  @Background
  protected void getTMDBMovies(String listType) {
    TmdbMovies movies = new TmdbApi(Constants.TMDB_API_KEY).getMovies();
  }*/

  private class MovieListsPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;

    public MovieListsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    private final int[] TITLES = {R.string.top_box_office, R.string.opening, R.string.in_theatres};

    @Override
    public int getCount() {
      return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return BoxOfficeFragment.create(Constants.BOX_OFFICE_PATH);
        case 1:
          return OpeningFragment.create(Constants.OPENING_PATH);
        default:
          return InTheatresFragment.create(Constants.IN_THEATRES_PATH);
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getString(TITLES[position]);
    }
  }
}
