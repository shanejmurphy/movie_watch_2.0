package com.movie.watch.model.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Cast implements Serializable {

  @SerializedName("cast_id")
  @Expose
  private Integer castId;
  @Expose
  private String character;
  @SerializedName("credit_id")
  @Expose
  private String creditId;
  @Expose
  private Integer id;
  @Expose
  private String name;
  @Expose
  private Integer order;
  @SerializedName("profile_path")
  @Expose
  private String profilePath;

  public Integer getCastId() {
    return castId;
  }

  public void setCastId(Integer castId) {
    this.castId = castId;
  }

  public String getCharacter() {
    return character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }

  public String getCreditId() {
    return creditId;
  }

  public void setCreditId(String creditId) {
    this.creditId = creditId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public String getProfilePath() {
    return profilePath;
  }

  public void setProfilePath(String profilePath) {
    this.profilePath = profilePath;
  }
}
