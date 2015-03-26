package com.movie.watch.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.movie.watch.R;
import com.movie.watch.adapter.ReviewListAdapter;
import com.movie.watch.busevents.ReviewsFetchEvent;
import com.movie.watch.model.Movie;
import com.movie.watch.model.reviews.Review;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_movie_review)
public class MovieReviewFragment extends BaseFragment {
  private static final String TAG = MovieReviewFragment.class.getSimpleName();

  @ViewById
  protected ListView reviewList;

  @Bean
  protected ReviewListAdapter reviewListAdapter;
  @Bean
  protected MovieInfoParser movieInfoParser;

  @FragmentArg
  protected Movie rottenTomatoesMovie;

  public static Fragment create(Movie rottenTomatoesMovie) {
    return MovieReviewFragment_.builder().rottenTomatoesMovie(rottenTomatoesMovie).build();
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return null;
  }

  @AfterViews
  public void afterViews() {
    reviewListAdapter.notifyDataSetChanged();
    reviewList.setAdapter(reviewListAdapter);
    //movieSelectionProgressBar.setVisibility(View.GONE);
  }

  @ItemClick(R.id.reviewList)
  public void movieListItemClicked(Review review) {
    openReviewLink(review);
  }

  private void openReviewLink(Review review) {
    Uri uri = Uri.parse(review.getLinks().getReview());
    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }

  public void onEventMainThread(ReviewsFetchEvent reviewsFetchEvent) {
    EventBus.getDefault().removeStickyEvent(reviewsFetchEvent);
    reviewListAdapter.setReviews(reviewsFetchEvent.getReviews().getReviews());
    reviewListAdapter.notifyDataSetChanged();
    reviewList.setAdapter(reviewListAdapter);
    //movieSelectionProgressBar.setVisibility(View.GONE);
  }
}
