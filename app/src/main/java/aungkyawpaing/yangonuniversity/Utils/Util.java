package aungkyawpaing.yangonuniversity.utils;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.ArrayList;

import aungkyawpaing.yangonuniversity.models.Department;

/**
 * Created by Vincent on 13-May-15.
 */
public class Util {

  public static ArrayList<Department> SortDepartment(ArrayList<Department> dept_list) {
    for (int i = 0, size = dept_list.size(); i < size - 1; i++) {
      for (int j = 0; j < size - i - 1; j++) {
        Department department = dept_list.get(j);
        Department nextDepartment = dept_list.get(j + 1);
        if (department.getDept_name().compareTo(nextDepartment.getDept_name()) > 0) {
          dept_list.set(j, nextDepartment);
          dept_list.set(j + 1, department);
        }
      }
    }
    return dept_list;
  }

  public static String unescape(String description) {
    return description.replaceAll("\\\\n", "\\\n").replaceAll("\\\\t", "\\\t");
  }

  public static boolean checkifPlayServiceAvaiable(Context context, Activity activity) {
    int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

    if (errorCode != ConnectionResult.SUCCESS) {
      GooglePlayServicesUtil.getErrorDialog(errorCode, activity, 0).show();
      return false;
    }

    return true;
  }
}
