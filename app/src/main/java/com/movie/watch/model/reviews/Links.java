package com.movie.watch.model.reviews;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Links {

  @Expose
  private String review;

  public String getReview() {
    return review;
  }

  public void setReview(String review) {
    this.review = review;
  }
}
