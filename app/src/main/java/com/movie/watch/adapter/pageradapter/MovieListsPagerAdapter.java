package com.movie.watch.adapter.pageradapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.movie.watch.R;
import com.movie.watch.constants.Constants;
import com.movie.watch.fragment.BoxOfficeFragment;
import com.movie.watch.fragment.InTheatresFragment;
import com.movie.watch.fragment.OpeningFragment;
import com.movie.watch.fragment.UpcomingFragment;
import com.movie.watch.model.MovieListType;

public class MovieListsPagerAdapter extends FragmentStatePagerAdapter {

  private final int[] TITLES = {R.string.top_box_office, R.string.in_theatres, R.string.opening, R.string.coming_soon };
  private static final int PAGE_COUNT = 4;
  private Context context;

  public MovieListsPagerAdapter(FragmentManager fm, Context context) {
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
        return BoxOfficeFragment.create(MovieListType.MOVIES, Constants.BOX_OFFICE_PATH);
      case 1:
        return InTheatresFragment.create(MovieListType.MOVIES, Constants.IN_THEATRES_PATH);
      case 2:
        return OpeningFragment.create(MovieListType.MOVIES, Constants.OPENING_PATH);
      case 3:
      default:
        return UpcomingFragment.create(MovieListType.MOVIES, Constants.UPCOMING_PATH);
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return context.getString(TITLES[position]);
  }
}