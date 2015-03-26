package com.movie.watch.busevents;

import com.movie.watch.model.credits.Credits;

public class CreditsFetchEvent {
    private Credits credits;

    public CreditsFetchEvent(Credits credits) {
      this.credits = credits;
    }

    public Credits getCredits() {
      return credits;
    }

}
