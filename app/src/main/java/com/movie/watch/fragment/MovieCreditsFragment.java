package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.CreditGridAdapter;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.CreditsErrorEvent;
import com.movie.watch.busevents.CreditsFetchEvent;
import com.movie.watch.model.credits.Cast;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_credits)
public class MovieCreditsFragment extends BaseFragment {
  private static final String TAG = MovieCreditsFragment.class.getSimpleName();

  @ViewById
  protected ProgressBar creditsProgressBar;
  @ViewById
  protected GridView creditsGrid;
  @ViewById
  protected TextView noCreditsText;

  @Bean
  protected CreditGridAdapter creditGridAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @InstanceState
  protected ArrayList<Cast> castList;

  public static Fragment create() {
    return MovieCreditsFragment_.builder().build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    if (castList == null) {
      creditsProgressBar.setVisibility(View.VISIBLE);
      creditsGrid.setVisibility(View.GONE);
    } else {
      displayCredits();
    }
  }

  public void onEventMainThread(CreditsFetchEvent creditsFetchEvent) {
    EventBus.getDefault().removeStickyEvent(creditsFetchEvent);
    castList = creditsFetchEvent.getCredits().getCast() == null ? new ArrayList<>() : (ArrayList) creditsFetchEvent.getCredits().getCast();
    displayCredits();
  }

  public void onEventMainThread(CreditsErrorEvent creditsErrorEvent) {
    EventBus.getDefault().removeStickyEvent(creditsErrorEvent);
    creditsProgressBar.setVisibility(View.GONE);
    noCreditsText.setText(R.string.fetch_error_text);
    noCreditsText.setVisibility(View.VISIBLE);
  }

  private void displayCredits() {
    creditsProgressBar.setVisibility(View.GONE);
    if (castList.isEmpty()) {
      noCreditsText.setVisibility(View.VISIBLE);
      return;
    }
    creditGridAdapter.setCastList(castList);
    creditGridAdapter.notifyDataSetChanged();
    creditsGrid.setAdapter(creditGridAdapter);
    creditsGrid.setVisibility(View.VISIBLE);
  }
}
