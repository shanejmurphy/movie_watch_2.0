package com.movie.watch.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.movie.watch.busevents.LocationConnectionFailedEvent;
import com.movie.watch.preferences.Prefs_;
import com.movie.watch.service.MovieFetchingService_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import de.greenrobot.event.EventBus;

@EBean(scope = EBean.Scope.Singleton)
public class LocationHelper implements
GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = LocationHelper.class.getSimpleName();
  private static final String DEFAULT_COUNTRY_CODE = "us";

  @RootContext
  Context context;

  @Pref
  protected Prefs_ prefs;

  private GoogleApiClient googleApiClient;
  private Double latitude;
  private Double longitude;
  private String countryCode;

  public synchronized void buildGoogleApiClient() {
    googleApiClient = new GoogleApiClient.Builder(context)
    .addConnectionCallbacks(this)
    .addOnConnectionFailedListener(this)
    .addApi(LocationServices.API)
    .build();
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.i(TAG, "Location Services connection connected");
    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    if (lastLocation != null) {
      latitude = lastLocation.getLatitude();
      longitude = lastLocation.getLongitude();
      String coords = latitude + "," + longitude;
      prefs.locationCoordinates().put(coords);
      MovieFetchingService_.intent(context).getGoogleAddress(coords).start();
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.w(TAG, "Location Services connection suspended");
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.w(TAG, "Location Services connection failed");
    EventBus.getDefault().postSticky(new LocationConnectionFailedEvent(connectionResult));
  }

  public String getLocationCoordinates() {
    if (TextUtils.isEmpty(prefs.locationCoordinates().get())) {
      return "";
    }
    return latitude + "," + longitude;
  }

  public void connect() {
    googleApiClient.connect();
  }

  public void disconnect() {
    googleApiClient.disconnect();
  }

  public String getCountryCode() {
    String storedCountryCode = prefs.countryCode().get();
    String fallbackCountryCode = storedCountryCode == null ? DEFAULT_COUNTRY_CODE : storedCountryCode;
    return countryCode == null ? fallbackCountryCode : countryCode.toLowerCase();
  }

  public void setCountryCode(String countryCode) {
    prefs.countryCode().put(countryCode);
    this.countryCode = countryCode;
  }
}
