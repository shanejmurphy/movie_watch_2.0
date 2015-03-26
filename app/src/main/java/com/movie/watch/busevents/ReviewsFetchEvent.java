package com.movie.watch.busevents;

import com.movie.watch.model.reviews.Reviews;

public class ReviewsFetchEvent {
    private Reviews reviews;

    public ReviewsFetchEvent(Reviews reviews) {
      this.reviews = reviews;
    }

    public Reviews getReviews() {
      return reviews;
    }

}
