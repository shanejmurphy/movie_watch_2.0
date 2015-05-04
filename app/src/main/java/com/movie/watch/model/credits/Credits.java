package com.movie.watch.model.credits;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Credits implements Serializable {

  @Expose
  private Integer id;
  @Expose
  private List<Cast> cast = new ArrayList<Cast>();
  @Expose
  private List<Crew> crew = new ArrayList<Crew>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Cast> getCast() {
    return cast;
  }

  public void setCast(List<Cast> cast) {
    this.cast = cast;
  }

  public List<Crew> getCrew() {
    return crew;
  }

  public void setCrew(List<Crew> crew) {
    this.crew = crew;
  }
}