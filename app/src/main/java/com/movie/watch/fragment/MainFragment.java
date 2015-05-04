package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.movie.watch.R;
import com.movie.watch.activity.MainActivity;
import com.movie.watch.adapter.pageradapter.DVDListsPagerAdapter;
import com.movie.watch.adapter.pageradapter.MovieListsPagerAdapter;
import com.movie.watch.model.MovieListType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_main)
public class MainFragment extends Fragment {

  @ViewById
  protected PagerSlidingTabStrip tabs;
  @ViewById
  protected ViewPager movieSelectionPager;

  @FragmentArg
  protected MovieListType movieListType;
  @FragmentArg
  protected String searchQuery;

  public static Fragment create(MovieListType movieListType, String searchQuery) {
    return MainFragment_.builder().movieListType(movieListType).searchQuery(searchQuery).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    setUpViewPager();
  }

  private void setUpViewPager() {
    switch(movieListType) {
      case SEARCH:
        tabs.setVisibility(View.GONE);
        movieSelectionPager.setVisibility(View.GONE);
        Fragment fragment = SearchResultsFragment.create(searchQuery);
        getFragmentManager().beginTransaction().add(R.id.searchListContainer, fragment, MainActivity.FRAGMENT_TAG).commit();
        break;
      case DVDS:
        movieSelectionPager.setOffscreenPageLimit(1);
        movieSelectionPager.setAdapter(new DVDListsPagerAdapter(getFragmentManager(), getActivity().getApplicationContext()));
        tabs.setViewPager(movieSelectionPager);
        break;
      case MOVIES:
      default:
        movieSelectionPager.setOffscreenPageLimit(1);
        movieSelectionPager.setAdapter(new MovieListsPagerAdapter(getFragmentManager(), getActivity().getApplicationContext()));
        tabs.setViewPager(movieSelectionPager);
    }
  }
}
