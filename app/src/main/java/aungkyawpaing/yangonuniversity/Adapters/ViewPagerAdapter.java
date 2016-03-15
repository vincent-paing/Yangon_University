package aungkyawpaing.yangonuniversity.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import aungkyawpaing.yangonuniversity.fragments.CampusMapFragment;
import aungkyawpaing.yangonuniversity.fragments.DepartmentFragment;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by vincentpaing on 15/3/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return DepartmentFragment.newInstance();
      case 1:
        return CampusMapFragment.newInstance();
      default:
        return null;
    }
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Department";
      case 1:
        return "Map";
      default:
        return super.getPageTitle(position);
    }
  }
}
