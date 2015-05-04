package com.movie.watch.busevents;

import android.text.Html;

public class ShowtimesFetchEvent {

  private String htmlBody;

  public ShowtimesFetchEvent(String htmlBody) {
    this.htmlBody = htmlBody;
  }

  public String getHtmlBody() {
    return Html.fromHtml(htmlBody).toString();
  }

  public void setHtmlBody(String htmlBody) {
    this.htmlBody = htmlBody;
  }
}
