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
import com.movie.watch.constants.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

  @ViewById
  protected PagerSlidingTabStrip tabs;
  @ViewById
  protected ViewPager movieSelectionPager;

  public static Fragment create() {
    return MainFragment_.builder().build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    movieSelectionPager.setOffscreenPageLimit(2);
    movieSelectionPager.setAdapter(new MovieListsPagerAdapter(getFragmentManager()));
    tabs.setViewPager(movieSelectionPager);
  }

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
