package com.movie.watch.adapter.pageradapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.fragment.ComingSoonFragment;
import com.movie.watch.fragment.CurrentReleasesFragment;
import com.movie.watch.fragment.NewReleasesFragment;
import com.movie.watch.fragment.TopRentalsFragment;
import com.movie.watch.model.MovieListType;

public class DVDListsPagerAdapter extends FragmentStatePagerAdapter {

  private final int[] TITLES = {R.string.top_rentals, R.string.current_releases, R.string.new_releases, R.string.upcoming};
  private static final int PAGE_COUNT = 4;
  private Context context;

  public DVDListsPagerAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return TopRentalsFragment.create(MovieListType.DVDS, Constants.TOP_RENTALS_PATH);
      case 1:
        return CurrentReleasesFragment.create(MovieListType.DVDS, Constants.CURRENT_RELEASES_PATH);
      case 2:
        return NewReleasesFragment.create(MovieListType.DVDS, Constants.NEW_RELEASES_PATH);
      default:
      case 3:
        return ComingSoonFragment.create(MovieListType.DVDS, Constants.UPCOMING_PATH);
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return context.getString(TITLES[position]);
  }
}