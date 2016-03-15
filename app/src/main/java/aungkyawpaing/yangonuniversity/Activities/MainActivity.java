package aungkyawpaing.yangonuniversity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import aungkyawpaing.yangonuniversity.adapters.ViewPagerAdapter;
import aungkyawpaing.yangonuniversity.fragments.CampusMapFragment;
import aungkyawpaing.yangonuniversity.fragments.DepartmentFragment;
import aungkyawpaing.yangonuniversity.fragments.SearchResultFragment;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.utils.Util;
import butterknife.ButterKnife;
import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.my_awesome_toolbar) Toolbar toolbar;
  @Bind(R.id.app_bar) AppBarLayout appBarLayout;
  @Bind(R.id.main_tab_layout) TabLayout mTabLayout;
  @Bind(R.id.pager) ViewPager mViewPager;

  public static FragmentManager fragmentManager;
  private SearchView searchView;
  private MenuItem searchViewMenuItem;
  private boolean isSearchResultFragmentLoaded = false;
  private Activity mActivity;
  private OnDataPass mDataPasser;

  //Activity Setting up Realted Method

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mActivity = this;
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.app_name);

    fragmentManager = getSupportFragmentManager();
    ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(pagerAdapter);
    mTabLayout.setupWithViewPager(mViewPager);

    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
        appBarLayout.setExpanded(true);
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }


  public void setupDataPasser(CampusMapFragment campusMapFragment) {
    mDataPasser = campusMapFragment;
  }


  //Menu Related Methods
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

  public void setAllMenuItemVisiblity(Menu menu, MenuItem exception, boolean visibility) {
    for (int i = 0; i < menu.size(); i++) {
      MenuItem item = menu.getItem(i);
      if (item != exception) {
        item.setVisible(visibility);
      }
    }
  }

  //Search Related Methods
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
    mTabLayout.getTabAt(1).select();
    fragmentManager.popBackStackImmediate();
    if (Util.checkifPlayServiceAvaiable(getApplicationContext(), mActivity)) {
      mDataPasser.onDataPass(query);
    }
  }


  @Override public void onBackPressed() {
    if (MenuItemCompat.isActionViewExpanded(searchViewMenuItem)) {
      MenuItemCompat.collapseActionView(searchViewMenuItem);
    } else {
      super.onBackPressed();
    }
  }


  public interface OnDataPass {
    public void onDataPass(String data);
  }
}
