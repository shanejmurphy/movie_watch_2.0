package com.movie.watch.utils.ui;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.ReviewListAdapter;
import com.movie.watch.model.reviews.Review;
import com.movie.watch.utils.MovieInfoParser;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_review)
public class ReviewItemView extends RelativeLayout {

  private Context context;

  @ViewById
  protected TextView reviewTitle;
  @ViewById
  protected TextView reviewText;
  @ViewById
  protected TextView reviewDate;

  @Bean
  protected MovieInfoParser movieInfoParser;

  public ReviewItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Review review, final ReviewListAdapter reviewListAdapter) {
    setReviewTitle(review);
    setReviewText(review);
    setReviewDate(review);
  }

  private void setReviewDate(Review review) {
    reviewDate.setText(movieInfoParser.getMediumDate(review.getDate()));
  }

  private void setReviewTitle(Review review) {
    int reviewDrawable = movieInfoParser.getReviewIcon(review.getFreshness());
    String critic = review.getCritic();
    String publication = review.getPublication();
    reviewTitle.setText(critic + " - " + publication);
    reviewTitle.setCompoundDrawablesWithIntrinsicBounds(reviewDrawable, 0, 0, 0);
  }

  private void setReviewText(Review review) {
    reviewText.setText(review.getQuote());
  }
}

