package com.movie.watch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movie.watch.model.Showtimes;
import com.movie.watch.utils.ui.ShowtimesItemView;
import com.movie.watch.utils.ui.ShowtimesItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class ShowtimesListAdapter extends BaseAdapter {
  private static final String TAG = ShowtimesListAdapter.class.getSimpleName();

  @RootContext
  protected Context context;

  private List<Showtimes> showtimes;

  @AfterInject
  public void afterInject() {
    showtimes = new ArrayList<>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ShowtimesItemView showtimesItemView;
    if (convertView == null) {
      showtimesItemView = ShowtimesItemView_.build(context);
    } else {
      showtimesItemView = (ShowtimesItemView) convertView;
    }
    showtimesItemView.bind(getItem(position), this);

    return showtimesItemView;
  }

  @Override
  public int getCount() {
    return showtimes.size();
  }

  @Override
  public Showtimes getItem(int position) {
    return showtimes.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<Showtimes> getShowtimes() {
    return showtimes;
  }

  public void setShowtimes(List<Showtimes> showtimes) {
    this.showtimes = showtimes;
  }
}