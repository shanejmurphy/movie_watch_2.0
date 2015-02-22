package com.movie.watch.activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.movie.watch.R;

public class BaseActivity extends ActionBarActivity {

  protected void setUpToolbar(Toolbar toolbar) {
    setSupportActionBar(toolbar);
    toolbar.setTitle(getString(R.string.app_name));
  }

  protected void replaceFragment(int containerId, Fragment fragment, String tag) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(containerId, fragment, tag).commit();
    fragmentManager.executePendingTransactions();
  }

  protected void loadAds(AdView adView) {
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest);
  }

  protected void setLollipopStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
    }
  }
}
