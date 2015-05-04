package com.movie.watch.model.tmdb;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Trailers implements Serializable {

  @Expose
  private List<Youtube> youtube = new ArrayList<Youtube>();
  @Expose
  private List<Object> quicktime = new ArrayList<Object>();

  public List<Object> getQuicktime() {
    return quicktime;
  }

  public void setQuicktime(List<Object> quicktime) {
    this.quicktime = quicktime;
  }

  public List<Youtube> getYoutubeTrailers() {
    return youtube;
  }

  public void setYouTubeTrailers(List<Youtube> youTubeTrailers) {
    this.youtube = youTubeTrailers;
  }
}