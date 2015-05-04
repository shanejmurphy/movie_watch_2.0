
package com.movie.watch.model.tmdb;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Releases implements Serializable {

  @Expose
  private Integer id;
  @Expose
  private List<Country> countries = new ArrayList<Country>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<Country> getCountries() {
    return countries;
  }

  public void setCountries(List<Country> countries) {
    this.countries = countries;
  }
}
