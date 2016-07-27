package aungkyawpaing.yangonuniversity.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vincentpaing on 19/6/16.
 */
public class FireBaseDatabaseManager {

  public static FirebaseDatabase database;
  public static DatabaseReference departmentRef;

  public static FirebaseDatabase getDatabase() {
    if (database == null) {
      database = FirebaseDatabase.getInstance();
      database.setPersistenceEnabled(true);
    }
    return database;
  }

  public static DatabaseReference getDepartmentReference() {
    if (departmentRef == null) {
      departmentRef = getDatabase().getReference("departments");
      departmentRef.keepSynced(true);
    }
    return departmentRef;
  }
}
