
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Genre {

  @Expose
  private Integer id;
  @Expose
  private String name;

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
}
