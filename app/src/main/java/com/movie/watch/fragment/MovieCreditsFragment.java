package com.movie.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.movie.watch.R;
import com.movie.watch.adapter.CreditGridAdapter;
import com.movie.watch.busevents.CreditsFetchEvent;
import com.movie.watch.model.credits.Cast;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_credits)
public class MovieCreditsFragment extends BaseFragment {
  private static final String TAG = MovieCreditsFragment.class.getSimpleName();

  @ViewById
  protected GridView creditsGrid;

  @Bean
  protected CreditGridAdapter creditGridAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;

  protected List<Cast> castList;

  public static Fragment create() {
    return MovieCreditsFragment_.builder().build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    castList = new ArrayList<>();
    creditGridAdapter.notifyDataSetChanged();
    creditsGrid.setAdapter(creditGridAdapter);
  }

  public void onEventMainThread(CreditsFetchEvent creditsFetchEvent) {
    EventBus.getDefault().removeStickyEvent(creditsFetchEvent);
    castList = creditsFetchEvent.getCredits().getCast();
    creditGridAdapter.setCastList(castList);
    creditGridAdapter.notifyDataSetChanged();
    creditsGrid.setAdapter(creditGridAdapter);
    //movieSelectionProgressBar.setVisibility(View.GONE);
  }
}
