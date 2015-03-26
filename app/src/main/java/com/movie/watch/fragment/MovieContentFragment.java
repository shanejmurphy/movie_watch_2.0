package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.movie.watch.R;
import com.movie.watch.model.Movie;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main)
public class MovieContentFragment extends Fragment {
  private static final String TAG = MovieContentFragment.class.getSimpleName();

  @ViewById
  protected PagerSlidingTabStrip tabs;
  @ViewById
  protected ViewPager movieSelectionPager;

  @FragmentArg
  protected Movie rottenTomatoesMovie;

  public static Fragment create(Movie rottenTomatoesMovie) {
    return MovieContentFragment_.builder().rottenTomatoesMovie(rottenTomatoesMovie).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    movieSelectionPager.setOffscreenPageLimit(2);
    movieSelectionPager.setAdapter(new MovieContentPagerAdapter(getFragmentManager()));
    tabs.setViewPager(movieSelectionPager);
  }

  private class MovieContentPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;

    public MovieContentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    private final int[] TITLES = {R.string.title_general, R.string.title_reviews, R.string.title_cast};

    @Override
    public int getCount() {
      return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return MovieInfoFragment.create(rottenTomatoesMovie);
        case 1:
          return MovieReviewFragment.create(rottenTomatoesMovie);
        default:
          return MovieCreditsFragment.create();
      }
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return getString(TITLES[position]);
    }
  }
}
