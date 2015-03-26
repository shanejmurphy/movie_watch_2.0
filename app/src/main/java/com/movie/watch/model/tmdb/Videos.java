package com.movie.watch.model.tmdb;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Videos {

  @Expose
  private Integer id;
  @Expose
  private List<Video> videos = new ArrayList<Video>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void setVideos(List<Video> videos) {
    this.videos = videos;
  }
}

