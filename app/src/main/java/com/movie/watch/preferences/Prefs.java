package com.movie.watch.preferences;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface Prefs {
  String countryCode();
  String locationCoordinates();
}
