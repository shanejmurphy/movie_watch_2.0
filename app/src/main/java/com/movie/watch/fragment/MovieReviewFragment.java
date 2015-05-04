package com.movie.watch.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.ReviewListAdapter;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.ReviewsErrorEvent;
import com.movie.watch.busevents.ReviewsFetchEvent;
import com.movie.watch.model.reviews.Review;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_review)
public class MovieReviewFragment extends BaseFragment {
  private static final String TAG = MovieReviewFragment.class.getSimpleName();

  @ViewById
  protected ProgressBar reviewsProgressBar;
  @ViewById
  protected ListView reviewList;
  @ViewById
  protected TextView noReviewsText;

  @Bean
  protected ReviewListAdapter reviewListAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @InstanceState
  protected ArrayList<Review> reviews;

  public static Fragment create() {
    return MovieReviewFragment_.builder().build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    if (reviews == null) {
      reviewsProgressBar.setVisibility(View.VISIBLE);
      reviewList.setVisibility(View.GONE);
    } else {
      displayReviews();
    }
  }

  @ItemClick(R.id.reviewList)
  public void movieListItemClicked(Review review) {
    openReviewLink(review);
    gaTrackerUtil.trackReviewSelectionEvent();
  }

  private void openReviewLink(Review review) {
    Uri uri = Uri.parse(review.getLinks().getReview());
    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }

  public void onEventMainThread(ReviewsFetchEvent reviewsFetchEvent) {
    EventBus.getDefault().removeStickyEvent(reviewsFetchEvent);
    reviews = reviewsFetchEvent.getReviews().getReviews() == null ? new ArrayList<>() : (ArrayList) reviewsFetchEvent.getReviews().getReviews();
    displayReviews();
  }

  public void onEventMainThread(ReviewsErrorEvent reviewsErrorEvent) {
    EventBus.getDefault().removeStickyEvent(reviewsErrorEvent);
    reviewsProgressBar.setVisibility(View.GONE);
    noReviewsText.setText(R.string.fetch_error_text);
    noReviewsText.setVisibility(View.VISIBLE);
  }

  private void displayReviews() {
    reviewsProgressBar.setVisibility(View.GONE);
    if (reviews.isEmpty()) {
      noReviewsText.setVisibility(View.VISIBLE);
      return;
    }
    reviewListAdapter.setReviews(reviews);
    reviewListAdapter.notifyDataSetChanged();
    reviewList.setAdapter(reviewListAdapter);
    reviewList.setVisibility(View.VISIBLE);
  }
}
