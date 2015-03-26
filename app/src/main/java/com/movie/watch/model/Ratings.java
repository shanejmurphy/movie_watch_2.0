
package com.movie.watch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Ratings implements Serializable {

  @SerializedName("critics_rating")
  @Expose
  public String criticsRating;
  @SerializedName("critics_score")
  @Expose
  public Integer criticsScore;
  @SerializedName("audience_rating")
  @Expose
  public String audienceRating;
  @SerializedName("audience_score")
  @Expose
  public Integer audienceScore;

  public String getCriticsRating() {
    return criticsRating;
  }

  public void setCriticsRating(String criticsRating) {
    this.criticsRating = criticsRating;
  }

  public Integer getCriticsScore() {
    return criticsScore;
  }

  public void setCriticsScore(Integer criticsScore) {
    this.criticsScore = criticsScore;
  }

  public String getAudienceRating() {
    return audienceRating;
  }

  public void setAudienceRating(String audienceRating) {
    this.audienceRating = audienceRating;
  }

  public Integer getAudienceScore() {
    return audienceScore;
  }

  public void setAudienceScore(Integer audienceScore) {
    this.audienceScore = audienceScore;
  }

  public boolean isCertifiedFresh() {
    return criticsScore >= 75;
  }

  public boolean isFresh() {
    return !isCertifiedFresh() && !isRotten();
  }

  public boolean isRotten() {
    return criticsScore < 60;
  }
}
