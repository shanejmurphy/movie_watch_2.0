
package com.movie.watch.model;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Cast implements Serializable {

  @Expose
  public String name;
  @Expose
  public String id;
  @Expose
  public List<String> characters = new ArrayList<String>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getCharacters() {
    return characters;
  }

  public void setCharacters(List<String> characters) {
    this.characters = characters;
  }
}
