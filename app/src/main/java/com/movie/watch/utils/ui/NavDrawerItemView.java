package com.movie.watch.utils.ui;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movie.watch.R;
import com.movie.watch.adapter.NavDrawerListAdapter;
import com.movie.watch.model.NavDrawerItem;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_nav_drawer)
public class NavDrawerItemView extends RelativeLayout {

  @ViewById
  protected TextView navDrawerItemText;

  public NavDrawerItemView(Context context) {
    super(context);
  }

  public void bind(final NavDrawerItem navDrawerItem, final NavDrawerListAdapter navDrawerListAdapter) {
    navDrawerItemText.setText(navDrawerItem.geTitle());
    navDrawerItemText.setCompoundDrawablesWithIntrinsicBounds(navDrawerItem.getDrawableResId(), 0, 0, 0);

  }
}

