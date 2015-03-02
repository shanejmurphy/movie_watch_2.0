package com.movie.watch.rest;


import com.movie.watch.utils.NetworkErrorHandler;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class DefaultCallback<T> implements Callback<T> {

  private NetworkErrorHandler networkErrorHandler;

/*  protected DefaultCallback(NetworkErrorHandler networkErrorHandler, ActivityStateChangeListener activityStateChangeListener) {
    this.networkErrorHandler = networkErrorHandler;
    this.activityStateChangeListener = activityStateChangeListener;
  }

  public DefaultCallback(ActivityStateChangeListener activityStateChangeListener) {
    this.activityStateChangeListener = activityStateChangeListener;
  }*/

  protected DefaultCallback(NetworkErrorHandler networkErrorHandler) {
    this.networkErrorHandler = networkErrorHandler;
  }

  protected DefaultCallback() {}

  @Override
  public final void success(T t, Response response) {
    //if (activityStateChangeListener.isAlive()) {
      onSuccess(t);
      onFinish();
    //}
  }

  @Override
  public final void failure(RetrofitError error) {
    //if (activityStateChangeListener.isAlive()) {
      onFailure(error);
      onFinish();
    //}
  }

  protected void onFailure(RetrofitError error) {
    if (networkErrorHandler != null) {
      networkErrorHandler.onNetworkError(error);
    }
  }

  protected abstract void onSuccess(T t);
  protected void onFinish() { }
}
