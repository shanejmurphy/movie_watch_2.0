package com.movie.watch.activity;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.movie.watch.R;
import com.movie.watch.fragment.MainFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  public static final String FRAGMENT_TAG = "mainFragmentContainer";

  @ViewById
  protected Toolbar mainToolbar;
  @ViewById
  protected DrawerLayout navDrawerLayout;
  @ViewById
  protected ListView navDrawerList;
  @ViewById
  protected AdView adView;

  private ActionBarDrawerToggle navDrawerToggle;

  @AfterViews
  protected void afterViews() {
    setUpActionBar();
    setUpNavDrawer();
    //loadAds(adView);
    setLollipopStatusBarColor();
    loadFragment();
  }

  private void setUpActionBar() {
    setSupportActionBar(mainToolbar);
  }

  private void setUpNavDrawer() {
    String [] navDrawerOptions = getResources().getStringArray(R.array.movie_selection_options);
    navDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item_nav_drawer, navDrawerOptions));
    navDrawerToggle = new ActionBarDrawerToggle(this, navDrawerLayout,
    R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        getActionBar().setTitle(getString(R.string.app_name));

        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        getActionBar().setTitle("Test");
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
  }

  @ItemClick
  protected void navDrawerList() {
    //TODO
  }

  private void loadFragment() {
    Fragment mainFragment = MainFragment.create();
    getSupportFragmentManager().beginTransaction().add(R.id.mainFragmentContainer, mainFragment).commit();
    //replaceFragment(R.id.mainFragmentContainer, mainFragment, FRAGMENT_TAG);
  }
}
