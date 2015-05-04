
package com.movie.watch.model;

public class NavDrawerItem {

  private String title;
  private int drawableResId;

  public NavDrawerItem(String title, int drawableResId){
    this.title = title;
    this.drawableResId = drawableResId;
  }

  public String geTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getDrawableResId() {
    return drawableResId;
  }

  public void setDrawableResId(int drawableResId) {
    this.drawableResId = drawableResId;
  }
}
