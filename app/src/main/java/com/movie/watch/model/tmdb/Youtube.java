package com.movie.watch.model.tmdb;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Youtube {
  @Expose
  private String name;
  @Expose
  private String size;
  @Expose
  private String source;
  @Expose
  private String type;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
