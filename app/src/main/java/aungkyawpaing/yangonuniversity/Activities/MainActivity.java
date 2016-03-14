package aungkyawpaing.yangonuniversity.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import aungkyawpaing.yangonuniversity.Fragments.CampusMapFragment;
import aungkyawpaing.yangonuniversity.Fragments.DepartmentFragment;
import aungkyawpaing.yangonuniversity.Fragments.SearchResultFragment;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Util;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

  @InjectView(R.id.my_awesome_toolbar) Toolbar toolbar;
  @InjectView(R.id.navigation_view) NavigationView mNavigationView;
  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @InjectView(R.id.app_bar) AppBarLayout mAppBarLayout;

  private final Handler mHandler = new Handler();
  private CharSequence mTitle;
  public static FragmentManager fragmentManager;
  private ActionBarDrawerToggle mDrawerToggle;
  private SearchView searchView;
  private MenuItem searchViewMenuItem;
  private boolean isSearchResultFragmentLoaded = false;
  private Activity mActivity;
  private Context mContext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

    mActivity = this;
    mContext = getApplicationContext();
    setSupportActionBar(toolbar);
    mTitle = getTitle();
    fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.container, DepartmentFragment.newInstance())
        .commit();

    setupNavigationDrawer();
  }

  private void setupNavigationDrawer() {
    mDrawerToggle = new ActionBarDrawerToggle(this,                    /* host Activity */
        mDrawerLayout,                    /* DrawerLayout object */
        toolbar, R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
        R.string.navigation_drawer_close  /* "close drawer" description for accessibility */) {
      @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if (MenuItemCompat.isActionViewExpanded(searchViewMenuItem)) {
          MenuItemCompat.collapseActionView(searchViewMenuItem);
        }
        invalidateOptionsMenu();
      }

      @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        updateTitle();
        invalidateOptionsMenu();
      }
    };
    mDrawerLayout.post(new Runnable() {
      @Override public void run() {
        mDrawerToggle.syncState();
      }
    });
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    mNavigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
              case R.id.navigation_item_1:
                mHandler.postDelayed(new Runnable() {
                  @Override public void run() {
                    fragmentManager.beginTransaction()
                        .replace(R.id.container, DepartmentFragment.newInstance())
                        .commit();
                  }
                }, 300);
                return true;
              case R.id.navigation_item_2:
                if (Util.checkifPlayServiceAvaiable(getApplicationContext(), mActivity)) {
                  mHandler.postDelayed(new Runnable() {
                    @Override public void run() {
                      fragmentManager.beginTransaction()
                          .replace(R.id.container, CampusMapFragment.newInstance())
                          .commit();
                      mAppBarLayout.setExpanded(true);
                    }
                  }, 300);
                }
                return true;
              default:
                return false;
            }
          }
        });
  }

  @Override public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onPostCreate(savedInstanceState, persistentState);
  }

  public void onSectionAttached(int number) {
    switch (number) {
      case 1:
        mTitle = getString(R.string.nav_departments);
        break;
      case 2:
        mTitle = getString(R.string.nav_map);
        break;
    }
    updateTitle();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  public void updateTitle() {
    if (mTitle != null) {
      toolbar.setTitle(mTitle);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_about, menu);

    searchViewMenuItem = menu.findItem(R.id.action_search);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) searchViewMenuItem.getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    MenuItemCompat.setOnActionExpandListener(searchViewMenuItem, new SearchViewExpandListener());
    searchView.setOnQueryTextListener(new SearchQueryListener());
    searchView.setQueryHint("Map Search...");
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.action_about)
            .setMessage(R.string.contents_about)
            .setPositiveButton("OK", null)
            .show();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  private class SearchViewExpandListener implements MenuItemCompat.OnActionExpandListener {

    @Override public boolean onMenuItemActionExpand(MenuItem menuItem) {
      setAllMenuItemVisiblity(toolbar.getMenu(), menuItem, false);
      return true;
    }

    @Override public boolean onMenuItemActionCollapse(MenuItem menuItem) {
      setAllMenuItemVisiblity(toolbar.getMenu(), menuItem, true);
      if (isSearchResultFragmentLoaded) {
        fragmentManager.popBackStackImmediate();
        isSearchResultFragmentLoaded = false;
      }
      return true;
    }
  }

  public void setAllMenuItemVisiblity(Menu menu, MenuItem exception, boolean visibility) {
    for (int i = 0; i < menu.size(); i++) {
      MenuItem item = menu.getItem(i);
      if (item != exception) {
        item.setVisible(visibility);
      }
    }
  }

  @Override public void onBackPressed() {
    if (MenuItemCompat.isActionViewExpanded(searchViewMenuItem)) {
      MenuItemCompat.collapseActionView(searchViewMenuItem);
    } else {
      super.onBackPressed();
    }
  }

  private class SearchQueryListener implements SearchView.OnQueryTextListener {

    @Override public boolean onQueryTextSubmit(String s) {
      if (isSearchResultFragmentLoaded) {
        fragmentManager.popBackStackImmediate();
      }
      fragmentManager.beginTransaction()
          .add(R.id.container, SearchResultFragment.newInstance(s))
          .addToBackStack("result")
          .commit();
      isSearchResultFragmentLoaded = true;
      searchView.clearFocus();
      return true;
    }

    @Override public boolean onQueryTextChange(String s) {
      return false;
    }
  }

  public void onSearchResultClick(String query) {
    mNavigationView.getMenu().getItem(1).setChecked(true);
    fragmentManager.popBackStackImmediate();
    if (Util.checkifPlayServiceAvaiable(getApplicationContext(), mActivity)) {
      fragmentManager.beginTransaction()
          .replace(R.id.container, CampusMapFragment.newInstance(query))
          .commit();
    }
  }
}
