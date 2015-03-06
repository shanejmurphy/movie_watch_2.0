
package com.movie.watch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class MovieList {

  @Expose
  private List<Movie> movies = new ArrayList<Movie>();
  @SerializedName("links")
  @Expose
  private AlternateLinks alternateLinks;
  @SerializedName("link_template")
  @Expose
  private String linkTemplate;

  public List<Movie> getMovies() {
    return movies;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

  public AlternateLinks getAlternateLinks() {
    return alternateLinks;
  }

  public void setAlternateLinks(AlternateLinks alternateLinks) {
    this.alternateLinks = alternateLinks;
  }

  public String getLinkTemplate() {
    return linkTemplate;
  }

  public void setLinkTemplate(String linkTemplate) {
    this.linkTemplate = linkTemplate;
  }
}
