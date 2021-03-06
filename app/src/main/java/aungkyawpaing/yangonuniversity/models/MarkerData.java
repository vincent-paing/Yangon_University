package aungkyawpaing.yangonuniversity.models;

import android.content.Context;
import aungkyawpaing.yangonuniversity.BuildConfig;

/**
 * Created by Vincent on 13-May-15.
 */
public class MarkerData {
  private int id;
  private String title;
  private double longitude;
  private double latitude;
  private String iconres;

  public MarkerData() {
    super();
  }

  public MarkerData(String title, double longitude, double latitude, String iconres) {
    super();
    this.title = title;
    this.longitude = longitude;
    this.latitude = latitude;
    this.iconres = iconres;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getIconres() {
    return iconres;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public int getIconres(Context context) {
    int id = context.getResources().getIdentifier(iconres, "drawable", BuildConfig.APPLICATION_ID);
    return id;
  }

  public void setIconres(String iconres) {
    this.iconres = iconres;
  }
}
