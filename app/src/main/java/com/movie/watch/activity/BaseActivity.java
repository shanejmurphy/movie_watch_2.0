package com.movie.watch.activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.movie.watch.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class BaseActivity extends AppCompatActivity {
  private static final String TAG = BaseActivity.class.getSimpleName();

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected AdView adView;

  protected void setUpActionBar(int title) {
    setUpActionBar(getString(title));
  }

  protected void setUpActionBar(String title) {
    setSupportActionBar(mainToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    setActionBarTitle(title);
  }

  protected void setActionBarTitle(int titleResId) {
    setActionBarTitle(getString(titleResId));
  }

  private void setActionBarTitle(String title) {
    getSupportActionBar().setTitle(title);
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

  protected void replaceFragment(int containerId, Fragment fragment, String tag) {
    getSupportFragmentManager().beginTransaction().replace(containerId, fragment, tag).commit();
  }

  protected void loadFragment(int containerId, Fragment fragment, String tag) {
    getSupportFragmentManager().beginTransaction().add(containerId, fragment, tag).commit();
  }
}
