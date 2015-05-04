package com.movie.watch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movie.watch.model.NavDrawerItem;
import com.movie.watch.utils.ui.NavDrawerItemView;
import com.movie.watch.utils.ui.NavDrawerItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class NavDrawerListAdapter extends BaseAdapter {
  private static final String TAG = NavDrawerListAdapter.class.getSimpleName();

  @RootContext
  protected Context context;

  private List<NavDrawerItem> navDrawerOptions;

  @AfterInject
  public void afterInject() {
    navDrawerOptions = new ArrayList<>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    NavDrawerItemView navDrawerItemView;
    if (convertView == null) {
      navDrawerItemView = NavDrawerItemView_.build(context);
    } else {
      navDrawerItemView = (NavDrawerItemView) convertView;
    }
    navDrawerItemView.bind(getItem(position), this);


    return navDrawerItemView;
  }

  @Override
  public int getCount() {
    return navDrawerOptions.size();
  }

  @Override
  public NavDrawerItem getItem(int position) {
    return navDrawerOptions.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<NavDrawerItem> getNavDrawerOptions() {
    return navDrawerOptions;
  }

  public void setNavDrawerOptions(List<NavDrawerItem> navDrawerOptions) {
    this.navDrawerOptions = navDrawerOptions;
  }
}