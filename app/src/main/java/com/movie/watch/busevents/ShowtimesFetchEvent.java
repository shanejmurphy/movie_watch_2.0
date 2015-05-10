package com.movie.watch.busevents;

public class ShowtimesFetchEvent {

  private String htmlBody;

  public ShowtimesFetchEvent(String htmlBody) {
    this.htmlBody = htmlBody;
  }

  public String getHtmlBody() {
    return htmlBody;
  }

  public void setHtmlBody(String htmlBody) {
    this.htmlBody = htmlBody;
  }
}
