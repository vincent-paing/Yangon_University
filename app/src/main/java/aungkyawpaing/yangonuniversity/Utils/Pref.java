package aungkyawpaing.yangonuniversity.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AKP on 8/23/2015.
 */
public class Pref {
  private static Pref pref;
  private SharedPreferences mPreference;
  private SharedPreferences.Editor mEditor;
  private Context mContext;
  private String mDialogStatus = "dialog_status";

  public Pref(Context context) {
    this.mContext = context;
    mPreference = context.getSharedPreferences(Constants.PREF_DATA, 0);
    mEditor = mPreference.edit();
  }

  public static synchronized Pref getInstance(Context context) {
    if (pref == null) {
      pref = new Pref(context);
    }
    return pref;
  }

  public void clearAll() {
    mEditor.clear();
    mEditor.commit();
  }

  public void setDialogStatus(boolean status) {
    mEditor.putBoolean(mDialogStatus, status);
    mEditor.commit();
  }

  public boolean getDialogStatus() {
    return mPreference.getBoolean(mDialogStatus, true);
  }
}
