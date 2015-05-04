package com.movie.watch.busevents;

public class FetchErrorEvent {
    private String listName;

    public FetchErrorEvent() {
    }

    public FetchErrorEvent(String listName) {
      this.listName = listName;
    }

    public String getListName() {
      return listName;
    }

}
