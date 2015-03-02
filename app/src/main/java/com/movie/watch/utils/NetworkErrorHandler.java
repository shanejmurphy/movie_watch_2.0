package com.movie.watch.utils;

import android.app.Activity;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import retrofit.RetrofitError;
import retrofit.client.Response;

@EBean
public class NetworkErrorHandler {
  private static final String TAG = NetworkErrorHandler.class.getSimpleName();
  protected Activity activity;

  public String getErrorMessage(int errorCode) {
    switch (errorCode) {
      default:
        return getDefaultErrorMessage();
    }
  }

  public String getErrorMessage(RetrofitError error) {
    Response response = error.getResponse();
    return response != null ? getErrorMessage(response.getStatus()) : getDefaultErrorMessage();
  }

  private String getDefaultErrorMessage() {
    return "Error - bllallaal";
  }

  public void onNetworkError(RetrofitError error) {
    Response response = error.getResponse();
    if (response != null) {
      Log.d(TAG, response.getBody().toString());
      //Crashlytics.log(response.getReason());
    }
  }
}
