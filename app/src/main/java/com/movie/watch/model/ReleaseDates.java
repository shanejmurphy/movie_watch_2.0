
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class ReleaseDates implements Serializable {

  @Expose
  private String theater;

  public String getTheater() {
    return theater;
  }

  public void setTheater(String theater) {
    this.theater = theater;
  }
}
