package aungkyawpaing.yangonuniversity.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vincentpaing on 13/6/16.
 */
public class FireBaseDatabaseHelper {

  static DatabaseReference databaseReference;

  public static DatabaseReference getDatabaseRefrenceInstance() {
    if (databaseReference == null) {
      FirebaseDatabase database = FirebaseDatabase.getInstance();
      databaseReference = database.getReference("departments");
    }
    return databaseReference;
  }


}
