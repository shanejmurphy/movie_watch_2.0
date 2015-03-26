
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class ProductionCompany {

  @Expose
  private String name;
  @Expose
  private Integer id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
