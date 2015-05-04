package com.movie.watch.busevents;

import com.google.android.gms.common.ConnectionResult;

public class LocationConnectionFailedEvent {

  private ConnectionResult connectionResult;

  public LocationConnectionFailedEvent(ConnectionResult connectionResult) {
    this.connectionResult = connectionResult;
  }

  public ConnectionResult getConnectionResult() {
    return connectionResult;
  }
}
