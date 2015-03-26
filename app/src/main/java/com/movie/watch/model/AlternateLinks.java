
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class AlternateLinks implements Serializable {

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
