
package com.movie.watch.model.reviews;

import com.google.gson.annotations.Expose;

public class AlternateLinks {

  @Expose
  private String self;
  @Expose
  private String next;
  @Expose
  private String alternate;
  @Expose
  private String rel;

  public String getSelf() {
    return self;
  }

  public void setSelf(String self) {
    this.self = self;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public String getAlternate() {
    return alternate;
  }

  public void setAlternate(String alternate) {
    this.alternate = alternate;
  }

  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }
}
