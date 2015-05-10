package com.movie.watch.application;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.movie.watch.BuildConfig;
import com.movie.watch.analytics.GoogleAnalyticsTracker;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import io.fabric.sdk.android.Fabric;

@EApplication
public class MovieWatchApplication extends Application {
  private static final String TAG = MovieWatchApplication.class.getSimpleName();
/*
  // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
  private static final String TWITTER_KEY = "uRfJltzoIjcBghU5bUkIUxrZP";
  private static final String TWITTER_SECRET = "xqaIebedL77wwGE5vF0eNp6EwpMWIRi90TLDQfqUQv8OmU2p1h";*/

  @Bean
  protected GoogleAnalyticsTracker analyticsTracker;

  @Override
  public void onCreate() {
    super.onCreate();
    initialiseFabric();

    this.analyticsTracker.init(getApplicationContext(), this);
  }

  private void initialiseFabric() {
    if (!Fabric.isInitialized()) {
      TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
      final Fabric fabric = new Fabric.Builder(this)
      .kits(new Crashlytics(), new Twitter(authConfig))
      .debuggable(true)
      .build();
      Fabric.with(fabric);
      Log.i(TAG, "Fabric Initialised");
    }
  }
}

