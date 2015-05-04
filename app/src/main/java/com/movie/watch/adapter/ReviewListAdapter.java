package com.movie.watch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movie.watch.model.reviews.Review;
import com.movie.watch.utils.ui.ReviewItemView;
import com.movie.watch.utils.ui.ReviewItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ReviewListAdapter extends BaseAdapter {
  private static final String TAG = ReviewListAdapter.class.getSimpleName();

  @RootContext
  protected Context context;

  private List<Review> reviews;

  @AfterInject
  public void afterInject() {
    reviews = new ArrayList<>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ReviewItemView reviewItemView;
    if (convertView == null) {
      reviewItemView = ReviewItemView_.build(context);
    } else {
      reviewItemView = (ReviewItemView) convertView;
    }
    reviewItemView.bind(getItem(position), this);

    return reviewItemView;
  }

  @Override
  public int getCount() {
    return reviews.size();
  }

  @Override
  public Review getItem(int position) {
    return reviews.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }
}