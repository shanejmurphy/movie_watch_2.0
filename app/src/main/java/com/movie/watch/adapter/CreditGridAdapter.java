package com.movie.watch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.movie.watch.model.credits.Cast;
import com.movie.watch.utils.ui.CastItemView;
import com.movie.watch.utils.ui.CastItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

@EBean
public class CreditGridAdapter extends BaseAdapter {
  private static final String TAG = CreditGridAdapter.class.getSimpleName();

  @RootContext
  protected Context context;

  private List<Cast> castList;

  @AfterInject
  public void afterInject() {
    castList = new ArrayList<>();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    CastItemView castItemView;
    if (convertView == null) {
      castItemView = CastItemView_.build(context);
    } else {
      castItemView = (CastItemView) convertView;
    }
    castItemView.bind(getItem(position), this);

    return castItemView;
  }

  @Override
  public int getCount() {
    Log.d(TAG, "count cast = " + castList.size());
    return castList.size();
  }

  @Override
  public Cast getItem(int position) {
    return castList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<Cast> getCastList() {
    return castList;
  }

  public void setCastList(List<Cast> castList) {
    this.castList = castList;
    Log.d(TAG, "Num Cast Members = " + castList.size());
  }
}