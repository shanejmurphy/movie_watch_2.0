package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.ShowtimesListAdapter;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.ShowtimesErrorEvent;
import com.movie.watch.busevents.ShowtimesFetchEvent;
import com.movie.watch.model.Showtimes;
import com.movie.watch.utils.LocationHelper;
import com.movie.watch.utils.ShowtimeParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_showtimes)
public class ShowtimesFragment extends BaseFragment {
  private static final String TAG = ShowtimesFragment.class.getSimpleName();

  @ViewById
  protected TextView noShowtimesText;
  @ViewById
  protected ListView showtimesList;
  @ViewById
  protected ProgressBar showtimesProgressBar;

  @Bean
  protected ShowtimesListAdapter showtimesListAdapter;
  @Bean
  protected ShowtimeParser showtimeParser;
  @Bean
  protected LocationHelper locationHelper;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @InstanceState
  ArrayList<Showtimes> showtimes;

  public static Fragment create() {
    return ShowtimesFragment_.builder().build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    if (showtimes == null) {
      displayLoading();
    } else {
      displayShowtimesList();
    }
  }

  private void displayLoading() {
    if (!TextUtils.isEmpty(locationHelper.getLocationCoordinates())) {
      showtimesProgressBar.setVisibility(View.VISIBLE);
    } else {
      noShowtimesText.setText(R.string.location_unavailable);
      noShowtimesText.setVisibility(View.VISIBLE);
    }
    showtimesList.setVisibility(View.GONE);
  }

  protected void displayShowtimesList() {
    showtimesProgressBar.setVisibility(View.GONE);
    if (showtimes.isEmpty()) {
      noShowtimesText.setText(R.string.not_showing);
      noShowtimesText.setVisibility(View.VISIBLE);
      return;
    }
    showtimesListAdapter.setShowtimes(showtimes);
    showtimesListAdapter.notifyDataSetChanged();
    showtimesList.setAdapter(showtimesListAdapter);
    showtimesList.setVisibility(View.VISIBLE);
  }

  public void onEventMainThread(ShowtimesFetchEvent showtimesFetchEvent) {
    EventBus.getDefault().removeStickyEvent(showtimesFetchEvent);
    showtimes = showtimeParser.parse(showtimesFetchEvent.getHtmlBody());
    displayShowtimesList();
  }

  public void onEventMainThread(ShowtimesErrorEvent showtimesErrorEvent) {
    EventBus.getDefault().removeStickyEvent(showtimesErrorEvent);
    showtimesProgressBar.setVisibility(View.GONE);
    noShowtimesText.setText(R.string.fetch_error_text);
    noShowtimesText.setVisibility(View.VISIBLE);
  }
}
