package com.movie.watch.analytics;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class GoogleAnalyticsTracker  {

  private static final String TAG = GoogleAnalyticsTracker.class.getSimpleName();
  private static final String USER_FLOW_GOOGLE_ANALYTICS_CATEGORY = "user_flow";
  private static Tracker tracker;
  private Context context;

  public void init(Context context, Application application) {
    if (tracker == null) {
      GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context);
      googleAnalytics.enableAutoActivityReports(application);
//TODO
/*      tracker = googleAnalytics.newTracker(R.xml.google_analytics);
      tracker.enableAutoActivityTracking(true);
      tracker.set("&tid", context.getString(R.string.googleAnalyticsId));*/

      this.context = context;
      Log.d(TAG, "Google Analytics Tracker initialised");
    }
  }

  public void trackEvent(int actionRes, int labelRes) {
    trackEvent(USER_FLOW_GOOGLE_ANALYTICS_CATEGORY, actionRes, labelRes);
  }

  public void trackEvent(String category, int actionRes, int labelRes) {
    trackEvent(category, getStringFromRes(actionRes), getStringFromRes(labelRes));
  }

  public void trackEvent(int actionRes, String label) {
    trackEvent(USER_FLOW_GOOGLE_ANALYTICS_CATEGORY, getStringFromRes(actionRes), label);
  }

  protected void trackEvent(String category, String action, String label) {
    Log.d(TAG, String.format("tracking event: action - %s, label - %s", action, label));
    tracker.send(new HitBuilders.EventBuilder()
    .setCategory(category)
    .setAction(action)
    .setLabel(label)
    .build());
  }

  private String getStringFromRes(int resId) {
    return context.getString(resId);
  }
}
