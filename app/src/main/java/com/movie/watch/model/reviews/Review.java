package com.movie.watch.model.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Review {

  @Expose
  private String critic;
  @Expose
  private String date;
  @Expose
  private String freshness;
  @Expose
  private String publication;
  @Expose
  private String quote;
  @Expose
  private Links links;
  @SerializedName("original_score")
  @Expose
  private String originalScore;

  public String getCritic() {
    return critic;
  }

  public void setCritic(String critic) {
    this.critic = critic;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getFreshness() {
    return freshness;
  }

  public void setFreshness(String freshness) {
    this.freshness = freshness;
  }

  public String getPublication() {
    return publication;
  }

  public void setPublication(String publication) {
    this.publication = publication;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }

  public String getOriginalScore() {
    return originalScore;
  }

  public void setOriginalScore(String originalScore) {
    this.originalScore = originalScore;
  }
}
