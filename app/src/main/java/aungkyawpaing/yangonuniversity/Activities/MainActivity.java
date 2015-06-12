package aungkyawpaing.yangonuniversity.Activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import aungkyawpaing.yangonuniversity.Fragments.CampusMapFragment;
import aungkyawpaing.yangonuniversity.Fragments.DepartmentFragment;
import aungkyawpaing.yangonuniversity.Fragments.NavigationDrawerFragment;
import aungkyawpaing.yangonuniversity.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity {

  @InjectView(R.id.my_awesome_toolbar) Toolbar toolbar;
  @InjectView(R.id.navigation_view) NavigationView mNavigationView;
  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;

  private final Handler mHandler = new Handler();
  private CharSequence mTitle;
  public static FragmentManager fragmentManager;
  private ActionBarDrawerToggle mDrawerToggle;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);

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
                mHandler.postDelayed(new Runnable() {
                  @Override public void run() {
                    fragmentManager.beginTransaction()
                        .replace(R.id.container, CampusMapFragment.newInstance())
                        .commit();
                  }
                }, 300);
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
    toolbar.setTitle(mTitle);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_about, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_about:
        new MaterialDialog.Builder(this).title(R.string.action_about)
            .content(R.string.contents_about)
            .positiveText("OK")
            .show();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }
}
