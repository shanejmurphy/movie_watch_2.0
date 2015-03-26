
package com.movie.watch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie implements Serializable {

  @Expose
  public String id;
  @Expose
  public String title;
  @Expose
  public Integer year;
  @SerializedName("mpaa_rating")
  @Expose
  public String mpaaRating;
  @Expose
  public Integer runtime;
  @SerializedName("critics_consensus")
  @Expose
  public String criticsConsensus;
  @SerializedName("release_dates")
  @Expose
  public ReleaseDates releaseDates;
  @Expose
  public Ratings ratings;
  @Expose
  public String synopsis;
  @Expose
  public Posters posters;
  @SerializedName("abridged_cast")
  @Expose
  public List<Cast> cast = new ArrayList<Cast>();
  @Expose
  public Links links;
  @SerializedName("alternate_ids")
  @Expose
  public AlternateIds alternateIds;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getMpaaRating() {
    return mpaaRating;
  }

  public void setMpaaRating(String mpaaRating) {
    this.mpaaRating = mpaaRating;
  }

  public Integer getRuntime() {
    return runtime;
  }

  public void setRuntime(Integer runtime) {
    this.runtime = runtime;
  }

  public String getCriticsConsensus() {
    return criticsConsensus;
  }

  public void setCriticsConsensus(String criticsConsensus) {
    this.criticsConsensus = criticsConsensus;
  }

  public ReleaseDates getReleaseDates() {
    return releaseDates;
  }

  public void setReleaseDates(ReleaseDates releaseDates) {
    this.releaseDates = releaseDates;
  }

  public Ratings getRatings() {
    return ratings;
  }

  public void setRatings(Ratings ratings) {
    this.ratings = ratings;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public Posters getPosters() {
    return posters;
  }

  public void setPosters(Posters posters) {
    this.posters = posters;
  }

  public List<Cast> getCast() {
    return cast;
  }

  public void setCast(List<Cast> cast) {
    this.cast = cast;
  }

  public Links getLinks() {
    return links;
  }

  public void setLinks(Links links) {
    this.links = links;
  }

  public AlternateIds getAlternateIds() {
    return alternateIds;
  }

  public void setAlternateIds(AlternateIds alternateIds) {
    this.alternateIds = alternateIds;
  }
}
