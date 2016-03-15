package aungkyawpaing.yangonuniversity.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import aungkyawpaing.yangonuniversity.models.Department;
import aungkyawpaing.yangonuniversity.models.MarkerData;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;

/**
 * Created by Vincent on 13-May-15.
 */
public class Database extends SQLiteAssetHelper {

  private SQLiteDatabase database;
  private static final String DATABASE_NAME = "yagonuniversity.db";
  private static final int DATABASE_VERSION = 2;
  private static Database mDatabase;

  public static Database getDatbase(Context context) {
    if (mDatabase == null) {
      mDatabase = new Database(context);
    }
    return mDatabase;
  }

  public Database(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    setForcedUpgrade();
  }

  public ArrayList<Department> getAllDepartment() {
    String get_all_dept = "SELECT * FROM tbl_department";
    database = getReadableDatabase();
    Cursor c = database.rawQuery(get_all_dept, null);
    ArrayList<Department> department_list = new ArrayList<Department>(c.getCount());
    if (c.moveToFirst()) {
      do {
        Department temp_Department = new Department();
        temp_Department.setDept_name(c.getString(0));
        temp_Department.setDept_info(c.getString(1));
        temp_Department.setDept_img(c.getString(2));
        temp_Department.setDept_location(c.getString(3));
        department_list.add(temp_Department);
      } while (c.moveToNext());
    }
    database.close();
    c.close();
    department_list = Util.SortDepartment(department_list);
    return department_list;
  }

  public ArrayList<MarkerData> getallMarkers() {
    String get_all_marker = "SELECT * FROM tbl_marker";
    database = getReadableDatabase();
    Cursor c = database.rawQuery(get_all_marker, null);
    ArrayList<MarkerData> markerdata_list = new ArrayList<MarkerData>(c.getCount());
    if (c.moveToFirst()) {
      do {
        MarkerData markerdata = new MarkerData();
        markerdata.setTitle(c.getString(0));
        markerdata.setLatitude(c.getDouble(1));
        markerdata.setLongitude(c.getDouble(2));
        markerdata.setIconres(c.getString(3));
        markerdata_list.add(markerdata);
      } while (c.moveToNext());
    }

    database.close();
    c.close();
    return markerdata_list;
  }
}
