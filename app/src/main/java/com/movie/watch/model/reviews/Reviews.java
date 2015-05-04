package com.movie.watch.model.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Reviews implements Serializable {

  @Expose
  private Integer total;
  @Expose
  private List<Review> reviews = new ArrayList<Review>();
  @Expose
  private AlternateLinks links;
  @SerializedName("link_template")
  @Expose
  private String linkTemplate;

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }

  public AlternateLinks getLinks() {
    return links;
  }

  public void setLinks(AlternateLinks links) {
    this.links = links;
  }

  public String getLinkTemplate() {
    return linkTemplate;
  }

  public void setLinkTemplate(String linkTemplate) {
    this.linkTemplate = linkTemplate;
  }
}
