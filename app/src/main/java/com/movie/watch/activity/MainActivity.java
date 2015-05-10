package com.movie.watch.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.movie.watch.R;
import com.movie.watch.adapter.NavDrawerListAdapter;
import com.movie.watch.analytics.GoogleAnalyticsTrackerUtil;
import com.movie.watch.busevents.GoogleAddressFetchEvent;
import com.movie.watch.busevents.LocationConnectionFailedEvent;
import com.movie.watch.fragment.MainFragment;
import com.movie.watch.model.MovieListType;
import com.movie.watch.model.NavDrawerItem;
import com.movie.watch.model.address.Address;
import com.movie.watch.model.address.AddressComponent;
import com.movie.watch.model.address.Result;
import com.movie.watch.utils.LocationHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_main_menu)
public class MainActivity extends BaseActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  public static final String FRAGMENT_TAG = "mainFragmentContainer";
  //play services update required params
  private static final int REQUEST_RESOLVE_ERROR = 1001;
  private static final String DIALOG_ERROR = "dialog_error";

  @ViewById
  protected DrawerLayout navDrawerLayout;
  @ViewById
  protected ListView navDrawerList;

  @OptionsMenuItem
  protected MenuItem menuSearch;

  @Bean
  protected NavDrawerListAdapter navDrawerListAdapter;
  @Bean
  protected LocationHelper locationHelper;
  @Bean
  protected GoogleAnalyticsTrackerUtil gaTrackerUtil;

  @SystemService
  protected SearchManager searchManager;

  @InstanceState
  protected MovieListType movieListType;
  @InstanceState
  protected int title;

  private ActionBarDrawerToggle navDrawerToggle;
  private boolean isResolvingError = false;

  @AfterInject
  protected void afterInject() {
    locationHelper.buildGoogleApiClient();
    locationHelper.connect();
    gaTrackerUtil.trackScreen(R.string.ga_screen_movie_pager);
  }

  @AfterViews
  protected void afterViews() {
    setUpNavDrawer();
    setLollipopStatusBarColor();
    loadAds(adView);
    if (movieListType == null) {
      title = R.string.title_theaters;
      movieListType = MovieListType.MOVIES;
      loadFragment(R.id.mainFragmentContainer, MainFragment.create(movieListType, null), FRAGMENT_TAG);
    }
    setUpActionBar(title);
    syncDrawerState();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    SearchView searchView = (SearchView) menuSearch.getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
    return true;
  }

  private void setUpNavDrawer() {
    ArrayList<NavDrawerItem> navDrawerItems;
    String[] navDrawerOptions = getResources().getStringArray(R.array.nav_drawer_options);
    TypedArray navDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    navDrawerItems = new ArrayList<>();
    navDrawerItems.add(new NavDrawerItem(navDrawerOptions[0], navDrawerIcons.getResourceId(0, -1)));
    //navDrawerItems.add(new NavDrawerItem(navDrawerOptions[1], navDrawerIcons.getResourceId(1, -1)));
    // Recycle the typed array
    navDrawerIcons.recycle();

    navDrawerListAdapter.setNavDrawerOptions(navDrawerItems);
    navDrawerList.setAdapter(navDrawerListAdapter);
    navDrawerToggle = new ActionBarDrawerToggle(this, navDrawerLayout, R.string.title_nav_drawer, title) {

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
      }

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        getSupportActionBar().setTitle(getString(R.string.title_nav_drawer));
        invalidateOptionsMenu();
      }
    };

    navDrawerToggle.setDrawerIndicatorEnabled(true);
    navDrawerLayout.setDrawerListener(navDrawerToggle);
  }

  @ItemClick
  protected void navDrawerList(int position) {
    switch (position) {
      default:
      case 0:
        new MaterialDialog.Builder(this)
        .title(R.string.title_about)
        .content(R.string.content_about)
        .backgroundColorRes(R.color.white)
        .titleColorRes(R.color.primary_text)
        .contentColorRes(R.color.secondary_text)
        .dividerColor(R.color.accent)
        .cancelable(true)
        .show();
    }
    navDrawerLayout.closeDrawers();
  }

  private void syncDrawerState() {
    if (navDrawerToggle == null) {
      return;
    }
    navDrawerToggle.syncState();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Activate the navigation drawer toggle
    return navDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }

  @OptionsItem
  protected void menuTheater() {
    if (movieListType != MovieListType.MOVIES) {
      title = R.string.title_theaters;
      setActionBarTitle(title);
      movieListType = MovieListType.MOVIES;
      replaceFragment(R.id.mainFragmentContainer, MainFragment.create(movieListType, null), FRAGMENT_TAG);
      gaTrackerUtil.trackMovieTypeSelectionEvent(R.string.title_theaters);
    }
  }

  @OptionsItem
  protected void menuDvd() {
    if (movieListType != MovieListType.DVDS) {
      title = R.string.title_dvds;
      setActionBarTitle(title);
      movieListType = MovieListType.DVDS;
      replaceFragment(R.id.mainFragmentContainer, MainFragment.create(movieListType, null), FRAGMENT_TAG);
      gaTrackerUtil.trackMovieTypeSelectionEvent(R.string.title_dvds);
    }
  }

  //Search stuff
  @Override
  protected void onNewIntent(Intent intent) {
    setIntent(intent);
    handleIntent(intent);
    gaTrackerUtil.trackMovieTypeSelectionEvent(R.string.search_title);
  }

  private void handleIntent(Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
      getSearchResults(intent);
    }
  }

  private void getSearchResults(Intent intent) {
    title = R.string.title_search_results;
    setActionBarTitle(title);
    movieListType = MovieListType.SEARCH;
    String searchQuery = intent.getStringExtra(SearchManager.QUERY);
    replaceFragment(R.id.mainFragmentContainer, MainFragment.create(movieListType, searchQuery), FRAGMENT_TAG);
  }

  public void onEventMainThread(GoogleAddressFetchEvent googleAddressFetchEvent) {
    EventBus.getDefault().removeStickyEvent(googleAddressFetchEvent);
    Address addresses = googleAddressFetchEvent.getAddresses();
    List<Result> addressResults = addresses.getResults();
    if (addressResults != null && !addressResults.isEmpty()) {
      for (AddressComponent addressComponent : addressResults.get(0).getAddressComponents()) {
        if (addressComponent.getTypes().contains("country")) {
          locationHelper.setCountryCode(addressComponent.getShortName().toLowerCase());
          locationHelper.disconnect();
          Log.i(TAG, "Country Code = " + locationHelper.getCountryCode());
        }
      }
    }
  }

  /* Creates a dialog for an error message */
  private void showErrorDialog(int errorCode) {
    // Create a fragment for the error dialog
    ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
    // Pass the error that should be displayed
    Bundle args = new Bundle();
    args.putInt(DIALOG_ERROR, errorCode);
    dialogFragment.setArguments(args);
    dialogFragment.show(getFragmentManager(), "errordialog");
  }

  public void onEventMainThread(LocationConnectionFailedEvent connectionFailedEvent) {
    EventBus.getDefault().removeStickyEvent(connectionFailedEvent);
    ConnectionResult connectionResult = connectionFailedEvent.getConnectionResult();
    if (isResolvingError) {
      // Already attempting to resolve an error.
      return;
    } else if (connectionResult.hasResolution()) {
      try {
        isResolvingError = true;
        connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
      } catch (IntentSender.SendIntentException e) {
        // There was an error with the resolution intent. Try again.
        locationHelper.connect();
      }
    } else {
      // Show dialog using GooglePlayServicesUtil.getErrorDialog()
      showErrorDialog(connectionResult.getErrorCode());
      isResolvingError = true;
    }
  }

  /* Called from ErrorDialogFragment when the dialog is dismissed. */
  public void onDialogDismissed() {
    isResolvingError = false;
  }

  /* A fragment to display an error dialog */
  public static class ErrorDialogFragment extends DialogFragment {
    public ErrorDialogFragment() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Get the error code and retrieve the appropriate dialog
      int errorCode = this.getArguments().getInt(DIALOG_ERROR);
      return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), REQUEST_RESOLVE_ERROR);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
      ((MainActivity) getActivity()).onDialogDismissed();
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().registerSticky(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
    locationHelper.disconnect();
  }
}
