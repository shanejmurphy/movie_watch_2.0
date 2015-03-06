
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class AlternateLinks {

  @Expose
  public String self;
  @Expose
  public String alternate;

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getAlternate() {
    return alternate;
  }

  public void setAlternate(String alternate) {
    this.alternate = alternate;
  }
}
