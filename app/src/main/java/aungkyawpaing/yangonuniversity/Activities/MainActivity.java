package aungkyawpaing.yangonuniversity.Activities;


import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import aungkyawpaing.yangonuniversity.Fragments.CampusMapFragment;
import aungkyawpaing.yangonuniversity.Fragments.DepartmentFragment;
import aungkyawpaing.yangonuniversity.Fragments.NavigationDrawerFragment;
import aungkyawpaing.yangonuniversity.R;
import aungkyawpaing.yangonuniversity.Utils.Constants;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @InjectView(R.id.my_awesome_toolbar) Toolbar toolbar;
//    @InjectView(R.id.toolbar_search) Toolbar searchtoolbar;
    private NavigationDrawerFragment mDrawerFragment;
    private final Handler mHandler = new Handler();
    private CharSequence mTitle;
    public static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        mTitle = getTitle();
        mDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mDrawerFragment.setUp(toolbar, R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        fragmentManager = getSupportFragmentManager();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction().replace(R.id.container, DepartmentFragment.newInstance()).commit();
                    }
                }, 300);
                break;
            case 1:
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction().replace(R.id.container, CampusMapFragment.newInstance()).commit();
                    }
                }, 300);
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1 :
                mTitle = getString(R.string.nav_departments);
                break;
            case 2:
                mTitle = getString(R.string.nav_map);
                break;
        }
        updateTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateTitle() {
        toolbar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about :
                new MaterialDialog.Builder(this)
                        .title(R.string.action_about)
                        .content(R.string.contents_about)
                        .positiveText("OK")
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
