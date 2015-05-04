package com.movie.watch.utils.ui;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.ShowtimesListAdapter;
import com.movie.watch.model.Showtimes;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_showtime)
public class ShowtimesItemView extends RelativeLayout {

  private Context context;

  @ViewById
  protected TextView theaterTitle;
  @ViewById
  protected TextView theaterAddress;
  @ViewById
  protected TextView theaterTimes;

  public ShowtimesItemView(Context context) {
    super(context);
    this.context = context;
  }

  public void bind(final Showtimes showtimes, final ShowtimesListAdapter showtimesListAdapter) {
    theaterTitle.setText(showtimes.getTheatre().getName());
    theaterAddress.setText(showtimes.getTheatre().getAddress());
    theaterTimes.setText(showtimes.displayTimes());
  }
}

